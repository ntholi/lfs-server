package com.breakoutms.lfs.server.preneed.payment;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
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

import com.breakoutms.lfs.server.common.UnitTest;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.PolicyRepository;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails.Type;
import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.preneed.pricing.utils.FuneralSchemeUtils;

import lombok.val;

@ExtendWith(MockitoExtension.class)
public class PolicyPaymentServiceUnitTest implements UnitTest {

	@Mock private PolicyPaymentRepository repo;
	@Mock private PolicyRepository policyRepo;
	@InjectMocks private PolicyPaymentService service;
	private final PolicyPayment entity;

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
		PolicyPayment response = service.save(entity, Set.of());
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
	
//	@Test //TODO
//	void should_not_pay_same_premium_twice() throws Exception{
//		when(repo.alreadyPaid(any(Policy.class), any(Period.class))).thenReturn(true);
//		
//		Throwable thrown = catchThrowable(() -> {
//			service.save(entity, List.of());
//		});
//		
//		assertThat(thrown).isInstanceOf(PaymentAlreadyMadeException.class);
//		assertThat(thrown).hasMessageContaining("already paid for period:");
//	}
//	
	@Test
	void should_save_unpaid_payments() throws Exception{
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);
		
		PolicyPaymentDetails payment = PolicyPaymentDetails
				.premiumOf(Period.now(), new BigDecimal(10));
		Set<UnpaidPolicyPayment> unpaids = Set.of(
			new UnpaidPolicyPayment(payment, entity.getPolicy())
		);
		
		PolicyPayment response = service.save(entity, unpaids);
		
		
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
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(createPolicy()));
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
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(createPolicy()));
		when(repo.getUnpaidPolicyPayment(anyString())).thenReturn(List.of());
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				Period.of(2020, Month.MARCH), policyNumber);
		
		assertThat(detailsList).hasSize(3);
		assertThat(detailsList).contains(PolicyPaymentDetails.penaltyOf(funeralScheme.getPenaltyFee()));
	}
	
	@Test
	void veryfy_that_unpaid_payments_are_edded_when_calling_getOwedPayments() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		Period period = Period.of(2020, Month.JANUARY);
		PolicyPaymentDetails paimentDetails = PolicyPaymentDetails.premiumOf(Period.of(2019, Month.DECEMBER), 
				policy.getPremiumAmount());
		List<UnpaidPolicyPayment> unpaidList = List.of(new UnpaidPolicyPayment(paimentDetails, policy));
		
		when(repo.getLastPayedPeriod(policy)).thenReturn(Optional.of(period));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(createPolicy()));
		when(repo.getUnpaidPolicyPayment(policyNumber)).thenReturn(unpaidList);
		
		List<PolicyPaymentDetails> detailsList = service.getOwedPayments(
				Period.of(2020, Month.MARCH), policyNumber);
		
		assertThat(detailsList).hasSize(4);
		assertThat(detailsList).contains(unpaidList.get(0).getPolicyPaymentDetails());
	}
	
	public static PolicyPayment createEntity() throws Exception {
		PolicyPayment entity = new PolicyPayment();
		Policy policy = createPolicy();
		entity.setId(5L);
		entity.setPaymentDate(LocalDateTime.now());
		entity.setPolicy(policy);
		entity.setPolicyPaymentDetails(policyPaymentInfoList(policy));
		return entity;
	}
	
	public static Set<PolicyPaymentDetails> policyPaymentInfoList(Policy policy) {
		Objects.requireNonNull(policy.getPremiumAmount());
		val stdAmount = policy.getPremiumAmount();
		return Set.of(
				new PolicyPaymentDetails(Type.PREMIUM, stdAmount, Period.of(LocalDate.now()))
		);
	}

	private static Policy createPolicy() throws Exception {
		val age = 43;
		FuneralScheme funeralScheme = FuneralSchemesJSON.byName("PLAN C");
		Premium premium = FuneralSchemeUtils.getPremium(funeralScheme, age);
		Policy policy = new Policy();
		policy.setPolicyNumber("101");
		policy.setNames("Thabo");
		policy.setSurname("Lebese");
		policy.setDateOfBirth(LocalDate.now().minusYears(age));
		policy.setFuneralScheme(funeralScheme);
		policy.setCoverAmount(premium.getCoverAmount());
		policy.setPremiumAmount(premium.getPremiumAmount());
		return policy;
	}
}
