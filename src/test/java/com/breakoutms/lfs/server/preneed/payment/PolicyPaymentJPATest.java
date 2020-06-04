package com.breakoutms.lfs.server.preneed.payment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DBUnit(allowEmptyFields = true) 
@DataSet({"funeral_scheme.xml", "policy.xml"})
public class PolicyPaymentJPATest {

	@Autowired PolicyPaymentRepository repo;
	@Autowired PolicyPaymentDetailsRepository paymentDetailsRepo;
	@Autowired PolicyRepository policyRepo;
	@Autowired PolicyPaymentService service;
	
	@Test
	void test_findLastPremiumId() {
		Period lastPeriod = Period.of(2020, Month.MARCH);
		Policy policy = policyRepo.findById("256070796").get();
		PolicyPayment payment = new PolicyPayment();
		payment.setPolicy(policy);
		payment.setPaymentDate(LocalDateTime.now());
		payment.setAmountTendered(new BigDecimal(500));
		var details = Set.of(PolicyPaymentDetails.premiumFor(policy, Period.of(2020, Month.JANUARY)), 
				PolicyPaymentDetails.premiumFor(policy, lastPeriod),
				PolicyPaymentDetails.premiumFor(policy, Period.of(2020, Month.FEBRUARY)),
				PolicyPaymentDetails.premiumFor(policy, Period.of(2019, Month.DECEMBER)));
		details.forEach(it -> {
			it.setPolicyPayment(payment);
			it.setPolicy(policy);
		});
		payment.setPolicyPaymentDetails(details);
		
		service.save(payment, policy.getPolicyNumber());
		
		paymentDetailsRepo.findAll().forEach(it ->{
			System.err.println();
			System.err.print("getPremiumPaymentId: "+it.getPremiumId());
			System.err.print(", policy: "+it.getPolicy().getId());
			System.err.print(", period: "+it.getPeriod());
			System.err.println();
		});
		
//		Period res = paymentDetailsRepo.findFirstByPolicyOrderByPremiumPaymentIdDesc(policy).get().getPeriod();
		
		Period res = paymentDetailsRepo.getLastPayedPeriod(policy.getId()).get();
		
		System.out.println(res);
		
		assertThat(res).isEqualTo(lastPeriod);
	}
	
	@Test
	void test_findPolicyPaymentDetailsByPaymentIds() {
		Policy policy = policyRepo.findById("256070796").get();
		PolicyPayment payment = new PolicyPayment();
		payment.setPolicy(policy);
		payment.setPaymentDate(LocalDateTime.now());
		payment.setAmountTendered(new BigDecimal(500));
		var details = Set.of(PolicyPaymentDetails.premiumFor(policy, Period.of(2020, Month.JANUARY)), 
				PolicyPaymentDetails.premiumFor(policy, Period.of(2020, Month.FEBRUARY)),
				PolicyPaymentDetails.premiumFor(policy, Period.of(2019, Month.DECEMBER)));
		details.forEach(it -> {
			it.setPolicyPayment(payment);
			it.setPolicy(policy);
		});
		payment.setPolicyPaymentDetails(details);
		service.save(payment, policy.getPolicyNumber());
		
		Set<String> premiumIds = details.stream()
				.filter(it -> it.getPeriod().getMonth() == Month.JANUARY 
					|| it.getPeriod().getMonth() == Month.DECEMBER)
				.map(it -> service.generatePremiumId(policy.getId(), it))
				.collect(Collectors.toSet());
		
		List<Month> paidMonths = repo.findPeriodsByPremiumIds(premiumIds)
				.stream()
				.map(Period::getMonth)
				.collect(Collectors.toList());
		
		assertThat(paidMonths).hasSize(2);
		assertThat(paidMonths).contains(Month.JANUARY, Month.DECEMBER);
	}
}
