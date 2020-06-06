package com.breakoutms.lfs.server.preneed.payment;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother.PlanType;
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
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

@ExtendWith(MockitoExtension.class)
public class PolicyPaymentServiceUnitTest {

	@Mock private PolicyPaymentRepository repo;
	@Mock private PolicyRepository policyRepo;
	@Mock private UnpaidPolicyPaymentRepository owedRepo;
	@Mock private PolicyPaymentDetailsRepository paymentDetailsRepo;
	@InjectMocks private PolicyPaymentService service;
	private final PolicyPayment entity;
	private final long ID = 5L;

	PolicyPaymentServiceUnitTest() throws Exception{
		entity = createEntity();
	}
	
	
	@Test
	void get_by_id() throws Exception {
		when(repo.findById(entity.getId())).thenReturn(Optional.of(entity));
		PolicyPayment response = service.get(entity.getId()).orElse(null);
		assertThat(response).isEqualTo(entity);
	}
	
	@Test
	void all() {
		PageRequest pagable = PageRequest.of(0, 1);
		when(repo.findAll(pagable)).thenReturn(new PageImpl<PolicyPayment>(List.of(entity), pagable, 1));
		
		Page<PolicyPayment> page = service.all(pagable);
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
		assertThat(page.get()).first().isEqualTo(entity);
	}

	@Test
	void save() throws Exception {
		String policyNumber = entity.getPolicy().getPolicyNumber();
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);
		when(policyRepo.findById(policyNumber)).thenReturn(Optional.of(entity.getPolicy()));
		
		PolicyPayment response = service.save(entity, entity.getPolicy().getId());
		
		assertThat(response)
			.isNotNull()
			.isEqualTo(entity);
	}
	
	@Test
	void update() throws Exception {
		var id = entity.getId();
		when(repo.existsById(id)).thenReturn(true);
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);

		PolicyPayment response = service.update(id, entity);
		assertThat(response)
			.isNotNull()
			.isEqualTo(entity);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = 123456L;
		String exMsg = ExceptionSupplier.notFound(PolicyPayment.class, unknownId).get().getMessage();
		
		when(repo.existsById(unknownId)).thenReturn(false);

		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new PolicyPayment());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	@Test
	void delete() {
		var id = entity.getId();
		service.delete(id);
		verify(repo).deleteById(id);
	}
	
	@Test
	void should_not_make_payment_for_deactivated_policy() {
		Policy policy = entity.getPolicy();
		policy.setStatus(PolicyStatus.DEACTIVATED);
		Period period = Period.now();
		
		when(paymentDetailsRepo.getLastPayedPeriod(policy.getId())).thenReturn(Optional.of(period));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		when(policyRepo.findById(policy.getId())).thenReturn(Optional.of(entity.getPolicy()));
		
		Throwable thrown = catchThrowable(() -> {
			service.save(entity, policy.getId());
		});
		
		assertThat(thrown).isInstanceOf(AccountNotActiveException.class);
		assertThat(thrown).hasMessageContaining(PolicyStatus.DEACTIVATED.name().toLowerCase());
		assertThat(thrown).hasMessageContaining(period.name());
	}
	
	@Test
	void should_not_pay_same_premium_twice() throws Exception{
		Policy policy = entity.getPolicy();
		Period p1 = Period.of(2020, Month.JANUARY);
		Period p2 = Period.of(2020, Month.MARCH);
		
		when(repo.findPeriodsByPremiumIds(anySet())).thenReturn(List.of(p1, p2));
		when(policyRepo.findById(policy.getId())).thenReturn(Optional.of(entity.getPolicy()));
		
		
		var details = Set.of(PolicyPaymentDetails.premiumOf(p1, new BigDecimal(30)), 
				PolicyPaymentDetails.premiumOf(Period.of(2020, Month.FEBRUARY), new BigDecimal(30)),
				PolicyPaymentDetails.premiumOf(p2, new BigDecimal(30)));

		PolicyPayment payment = new PolicyPayment();
		payment.setPolicyPaymentDetails(details);
		
		Throwable thrown = catchThrowable(() -> {
			service.save(payment, policy.getPolicyNumber());
		});
		
		assertThat(thrown).isInstanceOf(PaymentAlreadyMadeException.class);
		assertThat(thrown).hasMessageContaining("already paid for period:")
			.hasMessageContaining(p1.name())
			.hasMessageContaining(p2.name());
	}
	
	@Test 
	void should_save_unpaid_payments() throws Exception{
		Policy policy = entity.getPolicy();
		Period savedInOwed = Period.of(1992, Month.FEBRUARY);
		LocalDate today = LocalDate.now();
		Period lastMonth = Period.of(today.minusMonths(1));
		Period thisMonth = Period.of(today);
		
		var unpaids = List.of(
				new UnpaidPolicyPayment(PolicyPaymentDetails.premiumFor(policy, lastMonth)),
				new UnpaidPolicyPayment(PolicyPaymentDetails.premiumFor(policy, savedInOwed)));
		
		entity.setPolicyPaymentDetails(Set.of(
				PolicyPaymentDetails.premiumFor(policy, thisMonth)
		));
		
		when(owedRepo.findByPolicy(entity.getPolicy())).thenReturn(unpaids);
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);
		when(paymentDetailsRepo.getLastPayedPeriod(policy.getId())).thenReturn(Optional.of(lastMonth));
		when(policyRepo.findById(policy.getId())).thenReturn(Optional.of(entity.getPolicy()));
		
		service.save(entity, policy.getId());

		verify(owedRepo).saveAll(unpaids);
	}
	
	@Test
	void veryfy_the_correct_amount_of_owed_premiums_is_calculated() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		
		Optional<Period> lastPaidPeriod = Optional.of(Period.of(2020, Month.JANUARY));
		
		when(paymentDetailsRepo.getLastPayedPeriod(policy.getId())).thenReturn(lastPaidPeriod);
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(entity.getPolicy()));
		when(owedRepo.findByPolicy(policy)).thenReturn(List.of());
		
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
		Period period = Period.of(2020, Month.JANUARY);
		FuneralScheme funeralScheme = policy.getFuneralScheme();
		
		when(paymentDetailsRepo.getLastPayedPeriod(policy.getId())).thenReturn(Optional.of(period));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		when(owedRepo.findByPolicy(policy)).thenReturn(List.of());
		
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
		Period period = Period.of(2020, Month.JANUARY);
		PolicyPaymentDetails paimentDetails = PolicyPaymentDetails.premiumFor(policy, 
				Period.of(2019, Month.DECEMBER));
		paimentDetails.setPolicy(entity.getPolicy());
		List<UnpaidPolicyPayment> unpaidList = List.of(new UnpaidPolicyPayment(paimentDetails));
		
		when(owedRepo.findByPolicy(policy)).thenReturn(unpaidList);
		when(paymentDetailsRepo.getLastPayedPeriod(policy.getId())).thenReturn(Optional.of(period));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				policyNumber, Period.of(2020, Month.APRIL));
		
		assertThat(detailsList).hasSize(5); // [3 periods from Fab to Apr] + [1 unpaid payment] + [penalty]
		assertThat(detailsList).contains(unpaidList.get(0).getPolicyPaymentDetails());
	}
	
	@Test
	void test_getPolicyPaymentInquiry() {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		Period lastPaidPeriod = Period.of(2020, Month.JANUARY);
		Period currentPeriod = Period.of(2020, Month.APRIL);
		FuneralScheme funeralScheme = policy.getFuneralScheme();
		
		when(paymentDetailsRepo.getLastPayedPeriod(policy.getId())).thenReturn(Optional.of(lastPaidPeriod));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		when(owedRepo.findByPolicy(policy)).thenReturn(List.of());
		
		PolicyPaymentDetails penalty = PolicyPaymentDetails.penaltyOf(funeralScheme.getPenaltyFee());
		
		PolicyPaymentInquiry inquiry = service.getPolicyPaymentInquiry(
				policyNumber, currentPeriod);
		
		assertThat(inquiry.getPolicyNumber()).isEqualTo(policyNumber);
		assertThat(inquiry.getPolicyHolder()).isEqualTo(policy.getFullName());
		assertThat(inquiry.getPremium()).isEqualTo(policy.getPremiumAmount());
		assertThat(inquiry.getLastPayedPeriod()).isEqualTo(lastPaidPeriod);
		assertThat(inquiry.getPenaltyDue()).isEqualTo(penalty.getAmount());
		assertThat(inquiry.getPremiumDue()).isEqualTo(policy.getPremiumAmount()
				.multiply(new BigDecimal("3")));
		assertThat(inquiry.getPayments()).hasSize(4);
	}
	
	private PolicyPayment createEntity() {
		return new PolicyPaymentMother(PolicyMother.of(PlanType.Plan_C, 43))
				.id(ID)
				.withPremiumForCurrentMonth()
				.build();
	}
}
