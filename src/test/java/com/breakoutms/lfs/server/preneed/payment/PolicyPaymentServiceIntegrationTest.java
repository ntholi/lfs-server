package com.breakoutms.lfs.server.preneed.payment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.preneed.PolicyRepository;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
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
	
	private PolicyPayment entity;
	
	@BeforeEach
	void beforeEach() throws Exception {
		entity = createPolicyPayment();
	}
	
	@Test
	void verify_that_premiumId_is_generated_correctly() throws Exception {

	}
	
	@Test
	void sometest() throws Exception {
		BigDecimal amount = entity.getPolicy().getPremiumAmount();
		LocalDate today = LocalDate.now();
		var premium = PolicyPaymentDetails.premiumOf(Period.of(today), amount);

		entity.setPolicyPaymentDetails(Set.of(premium));

		String premiumId = entity.getPolicy().getPolicyNumber()
				+ String.valueOf(today.getYear()).substring(2)
				+ today.getMonthValue();

		var savedEntity = service.save(entity, Collections.emptySet());

		var payment = createPolicyPayment();
		payment.setPolicyPaymentDetails(Set.of(premium));
		service.save(entity, Collections.emptySet());

		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getId()).isNotNull();
		assertThat(savedEntity.getPolicyPaymentDetails()
				.iterator()
				.next()
				.getPremiumPaymentId()).isEqualTo(premiumId);
	}

	protected PolicyPayment createPolicyPayment() throws Exception {
		PolicyPayment entity = new PolicyPayment();
		Policy policy = policyRepository.findById("256070816").get();
		entity.setPolicy(policy);
		entity.setAmountTendered(new BigDecimal("30"));
		entity.setPaymentDate(LocalDateTime.now());
		entity.setPolicyPaymentDetails(PolicyPaymentServiceUnitTest.policyPaymentInfoList(policy));
		return entity;
	}
}
