package com.breakoutms.lfs.server.preneed.payment;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Objects;
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
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyStatus;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

@ExtendWith(MockitoExtension.class)
public class PolicyPaymentServiceUnitTest {

	@Mock private PolicyPaymentRepository repo;
	@Mock private PolicyRepository policyRepo;
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
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);
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
		Policy policy = new Policy();
		policy.setPolicyNumber("101");
		policy.setStatus(PolicyStatus.DEACTIVATED);
		entity.setPolicy(policy);
		Period period = Period.now();
		
		when(repo.getLastPayedPeriod(policy)).thenReturn(Optional.of(period));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		
		Throwable thrown = catchThrowable(() -> {
			service.save(entity, policy.getId());
		});
		
		assertThat(thrown).isInstanceOf(AccountNotActiveException.class);
		assertThat(thrown).hasMessageContaining(PolicyStatus.DEACTIVATED.name().toLowerCase());
		assertThat(thrown).hasMessageContaining(period.name());
	}
	
	@Test
	void should_not_pay_same_premium_twice() throws Exception{
		String policyNumber = "hello";
		Period p1 = Period.of(2020, Month.JANUARY);
		Period p2 = Period.of(2020, Month.MARCH);
		when(repo.findPeriodsByPaymentIds(anySet())).thenReturn(List.of(p1, p2));
		
		var details = Set.of(PolicyPaymentDetails.premiumOf(p1, new BigDecimal(30)), 
				PolicyPaymentDetails.premiumOf(Period.of(2020, Month.FEBRUARY), new BigDecimal(30)),
				PolicyPaymentDetails.premiumOf(p2, new BigDecimal(30)));
		details.forEach(it -> {
			it.setPolicyNumber(policyNumber);
		});
		PolicyPayment payment = new PolicyPayment();
		payment.setPolicyPaymentDetails(details);
		
		Throwable thrown = catchThrowable(() -> {
			service.save(payment, policyNumber);
		});
		
		assertThat(thrown).isInstanceOf(PaymentAlreadyMadeException.class);
		assertThat(thrown).hasMessageContaining("already paid for period:")
			.hasMessageContaining(p1.name())
			.hasMessageContaining(p2.name());
	}
	
	@Test 
	void should_save_unpaid_payments() throws Exception{ //TODO is this test really doing what it has to do?
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);
		
		PolicyPaymentDetails payment = PolicyPaymentDetails
				.premiumOf(Period.now(), new BigDecimal(10));
		Set<UnpaidPolicyPayment> unpaids = Set.of( 
			new UnpaidPolicyPayment(payment, entity.getPolicy())
		);
		
		PolicyPayment response = service.save(entity, entity.getPolicy().getId());
		
		assertThat(response)
			.isNotNull()
			.isEqualTo(entity);
	}
	
	@Test
	void veryfy_the_correct_amount_of_owed_premiums_is_returned() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		
		Optional<Period> period = Optional.of(Period.of(2020, Month.JANUARY));
		when(repo.getLastPayedPeriod(policy)).thenReturn(period);
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(entity.getPolicy()));
		when(repo.getUnpaidPolicyPayment(anyString())).thenReturn(List.of());
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				Period.of(2020, Month.MARCH), policyNumber);
		List<Period> periods = detailsList.stream()
				.map(PolicyPaymentDetails::getPeriod)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		
		assertThat(detailsList).hasSize(3); //Three because of penalty
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
		
		when(repo.getLastPayedPeriod(policy)).thenReturn(Optional.of(period));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		when(repo.getUnpaidPolicyPayment(anyString())).thenReturn(List.of());
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				Period.of(2020, Month.MARCH), policyNumber);
		
		assertThat(detailsList).hasSize(3); //[2 periods from Fab to March] + [penalty]
		assertThat(detailsList).contains(PolicyPaymentDetails.penaltyOf(funeralScheme.getPenaltyFee()));
	}
	
	@Test
	void veryfy_that_UnpaidPolicyPayments_are_added_when_calling_getOwedPayments() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		Period period = Period.of(2020, Month.JANUARY);
		PolicyPaymentDetails paimentDetails = PolicyPaymentDetails.premiumOf(Period.of(2019, Month.DECEMBER), 
				policy.getPremiumAmount());
		List<UnpaidPolicyPayment> unpaidList = List.of(new UnpaidPolicyPayment(paimentDetails, policy));
		
		when(repo.getLastPayedPeriod(policy)).thenReturn(Optional.of(period));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		when(repo.getUnpaidPolicyPayment(policyNumber)).thenReturn(unpaidList);
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				Period.of(2020, Month.MARCH), policyNumber);
		
		assertThat(detailsList).hasSize(4); // [2 periods from Fab to March] + [1 unpaid payment] + [penalty]
		assertThat(detailsList).contains(unpaidList.get(0).getPolicyPaymentDetails());
	}
	
	private PolicyPayment createEntity() {
		return new PolicyPaymentMother(PolicyMother.of(PlanType.Plan_C, 43))
				.id(ID)
				.withPremiumForCurrentMonth()
				.build();
	}
}
