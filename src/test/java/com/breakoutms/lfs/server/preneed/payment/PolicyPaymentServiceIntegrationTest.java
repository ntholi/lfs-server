package com.breakoutms.lfs.server.preneed.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyPaymentMother;
import com.breakoutms.lfs.server.exceptions.AccountNotActiveException;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.exceptions.PaymentAlreadyMadeException;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentInquiry;
import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyStatus;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DBUnit(allowEmptyFields = true) 
@DataSet({"funeral_scheme.xml", "policy.xml"})
public class PolicyPaymentServiceIntegrationTest {

	@Autowired BranchRepository branchRepo;
	@Autowired FuneralSchemeRepository funeralSchemeRepo;
	@Autowired PolicyRepository policyRepository;
	@Autowired PolicyPaymentService service;
	@Autowired PolicyPaymentRepository repo;
	@Autowired UnpaidPolicyPaymentRepository unpaidRepo;
	@Autowired EntityManager entityManager;
	
	private PolicyPayment entity;
	
	@BeforeEach
	void beforeEach() throws Exception {
		entity = createPolicyPayment(getPolicy());
		//TODO: @DataSet is not initiated when calling it in @BeforeEach when running test after the db has been
		// re-created. fix it
	}
	
	@Test
	void get_by_id() throws Exception {
		repo.save(entity);
		PolicyPayment response = service.get(entity.getId()).orElse(null);
		assertThat(response).isEqualTo(entity);
	}
	
	@Test
	void all() {
		PageRequest pagable = PageRequest.of(0, 1);
	
		repo.save(entity);
		
		Page<PolicyPayment> page = service.all(pagable);
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
		assertThat(page.get()).first().isEqualTo(entity);
	}

	@Test
	void save() throws Exception {
		PolicyPayment entity = new PolicyPaymentMother(getPolicy())
				.removeId()
				.withPremiumForCurrentMonth()
				.build();
		
		PolicyPayment response = service.save(entity, getPolicy().getId());
		
		assertThat(response)
			.isNotNull()
			.isEqualTo(entity);
	}
	
	@Test
	void update() throws Exception {

		var before = new BigDecimal("300");
		var after = new BigDecimal("400");
		
		entity.setAmountTendered(before);
		assertThat(entity.getAmountTendered()).isEqualTo(before);
		Entry<Long, PolicyPayment> result = persistAndGetCopy(entity);
		var copy = result.getValue();
		var id = result.getKey();
		
		copy.setAmountTendered(after);
		var updatedEntity = service.update(id, copy);
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(updatedEntity.getAmountTendered()).isEqualTo(after);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = 123456L;
		String exMsg = ExceptionSupplier.notFound(PolicyPayment.class, unknownId)
				.get().getMessage();

		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new PolicyPayment());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	@Test
	void delete() {
		repo.save(entity);
		
		var id = entity.getId();
		service.delete(id);
		
		assertThat(repo.findAll()).isEmpty();
	}
	
	@Test
	void verify_premiumId_is_generated_correctly() throws Exception {
		Policy policy = getPolicy();
		
		LocalDate today = LocalDate.now();
		String premiumId = policy.getPolicyNumber()
				+ String.valueOf(today.getYear()).substring(2)
				+ String.format("%02d", today.getMonthValue());

		PolicyPayment entity = service.save(new PolicyPaymentMother(policy)
				.removeId()
				.withPremiumForCurrentMonth()
				.build(), policy.getId());

		assertThat(entity).isNotNull();
		assertThat(entity.getId()).isNotNull();
		assertThat(entity.getPolicyPaymentDetails()
				.iterator()
				.next()
				.getPremiumId()).isEqualTo(premiumId);
	}
	
	@Test
	void should_not_make_payment_for_deactivated_policy() {
		Policy policy = entity.getPolicy();
		policy.setStatus(PolicyStatus.DEACTIVATED);
		
		policyRepository.save(policy);
		
		Throwable thrown = catchThrowable(() -> {
			service.save(entity, policy.getId());
		});
		
		assertThat(thrown).isInstanceOf(AccountNotActiveException.class);
		assertThat(thrown).hasMessageContaining(PolicyStatus.DEACTIVATED.name().toLowerCase());
	}
	
	@Test
	void should_not_pay_same_premium_twice() throws Exception{
		Policy policy = entity.getPolicy();
		Period period = Period.now();
		
		service.save(entity, policy.getId());
		
		Throwable thrown = catchThrowable(() -> {
			service.save(entity, policy.getId());
		});
		
		assertThat(thrown).isInstanceOf(PaymentAlreadyMadeException.class);
		assertThat(thrown).hasMessageContaining("already paid for period:")
			.hasMessageContaining(period.name());
	}
	
	@Test 
	void should_save_unpaid_payments() throws Exception{
		Policy policy = entity.getPolicy();
		Period savedInOwed = Period.of(1992, Month.FEBRUARY);
		LocalDate today = LocalDate.now();
		Period lastMonth = Period.of(today.minusMonths(1));
		Period thisMonth = Period.of(today);
		
		unpaidRepo.save(new UnpaidPolicyPayment(PolicyPaymentDetails.premiumFor(policy, savedInOwed)));
		
		entity.setPolicyPaymentDetails(Set.of(
				PolicyPaymentDetails.premiumFor(policy, lastMonth).forPolicyPayment(entity)
		));
		service.save(entity, policy.getId());
		
		assertThat(unpaidRepo.findAll().stream()
				.map(UnpaidPolicyPayment::getPeriod))
			.doesNotContain(lastMonth)
			.contains(thisMonth, savedInOwed);
	}

	@Test
	void veryfy_the_correct_amount_of_owed_premiums_is_calculated() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		
		List<PolicyPaymentDetails> owedPayments = service.getOwedPayments(
				policyNumber, Period.of(2020, Month.MARCH));
		
		List<Period> periods = owedPayments.stream()
				.map(PolicyPaymentDetails::getPeriod)
				.collect(Collectors.toList());
		
		assertThat(owedPayments).hasSize(2); // FEBRUARY and MARCH 
		assertThat(periods).hasSize(2);
		assertThat(periods).contains(Period.of(2020, Month.MARCH), 
				Period.of(2020, Month.FEBRUARY));
	}
	
	@Test
	void veryfy_that_penalty_is_added_correctly() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		FuneralScheme funeralScheme = policy.getFuneralScheme();
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				policyNumber, Period.of(2020, Month.APRIL));
		
		PolicyPaymentDetails penalty = PolicyPaymentDetails.penaltyOf(funeralScheme.getPenaltyFee());
		penalty.setPolicy(policy);
		
		assertThat(detailsList).hasSize(4); //[2 periods from Fab, March, Apr] + [penalty]
		assertThat(detailsList).contains(penalty);
		
		assertThat(service.getOwedPayments(policyNumber, Period.of(2020, Month.JUNE))
				.stream()
				.filter(PolicyPaymentDetails::isPenalty)
				.findFirst().get().getAmount()
		).isEqualTo(funeralScheme.getPenaltyFee().multiply(new BigDecimal(2)));
	}
	
	@Test
	void veryfy_that_UnpaidPolicyPayments_are_added_when_calling_getOwedPayments() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		PolicyPaymentDetails paimentDetails = PolicyPaymentDetails.premiumFor(policy, 
				Period.of(2019, Month.DECEMBER));
		paimentDetails.setPolicy(entity.getPolicy());
		
		UnpaidPolicyPayment unpaid = new UnpaidPolicyPayment(paimentDetails);
		unpaidRepo.saveAndFlush(unpaid);
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				policyNumber, Period.of(2020, Month.APRIL));
		
		assertThat(detailsList).hasSize(5); // [2 periods from Fab to Apr] + [1 unpaid payment] + [penalty]
		assertThat(detailsList).contains(unpaid.getPolicyPaymentDetails());
	}
	
	@Test
	void test_getPolicyPaymentInquiry() {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		Period period = Period.of(2020, Month.JANUARY);
		FuneralScheme funeralScheme = policy.getFuneralScheme();
		
		PolicyPaymentDetails penalty = PolicyPaymentDetails.penaltyOf(funeralScheme.getPenaltyFee());
		
		PolicyPaymentInquiry inquiry = service.getPolicyPaymentInquiry(
				policyNumber, Period.of(2020, Month.APRIL));
		
		assertThat(inquiry.getPolicyNumber()).isEqualTo(policyNumber);
		assertThat(inquiry.getPolicyHolder()).isEqualTo(policy.getFullName());
		assertThat(inquiry.getPremium()).isEqualTo(policy.getPremiumAmount());
		assertThat(inquiry.getLastPayedPeriod()).isEqualTo(period);
		assertThat(inquiry.getPenaltyDue()).isEqualTo(penalty.getAmount());
		assertThat(inquiry.getPremiumDue()).isEqualTo(policy.getPremiumAmount()
				.multiply(new BigDecimal("3")));
		assertThat(inquiry.getPayments()).hasSize(4);
	}
	
	protected Entry<Long, PolicyPayment> persistAndGetCopy(PolicyPayment entity) {		
		service.save(entity, getPolicy().getId());
		entityManager.flush();
		entityManager.clear();
		
		Long id = entity.getId();
		
		entity.setId(null);
//		return DeepCopy.copy(entity);
		return Map.entry(id, entity);
	}
	
	protected PolicyPayment createPolicyPayment(Policy policy) throws Exception {
		return new PolicyPaymentMother(policy)
				.removeId()
				.payment(null)
				.withPremiumForCurrentMonth()
				.build();
	}
	
	private Policy getPolicy() {
		Policy policy = policyRepository.findById("256070816").get();
		policy.setPassportNumber(null);
		return policy;
	}
}
