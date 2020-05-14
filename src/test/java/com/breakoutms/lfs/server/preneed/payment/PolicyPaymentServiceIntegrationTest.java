package com.breakoutms.lfs.server.preneed.payment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.preneed.PolicyRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;
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
	
	@BeforeEach
	void beforeEach() {
		
		policyRepository.findById("");
	}
	
//	
//	void unused_test() throws Exception {
//		BigDecimal amount = entity.getPolicy().getPremiumAmount();
//		LocalDate today = LocalDate.now();
//		var premium = PolicyPaymentDetails.premiumOf(Period.of(today), amount);
//
//		entity.setPolicyPaymentDetails(Set.of(premium));
//
//		String premiumId = entity.getPolicy().getPolicyNumber()
//				+ String.valueOf(today.getYear()).substring(2)
//				+ today.getMonthValue();
//
//		var savedEntity = service.save(entity, Collections.emptySet());
//
//		var payment = createPolicyPayment();
//		payment.setPolicyPaymentDetails(Set.of(premium));
//		service.save(entity, Collections.emptySet());
//
//		assertThat(savedEntity).isNotNull();
//		assertThat(savedEntity.getId()).isNotNull();
//		assertThat(savedEntity.getPolicyPaymentDetails()
//				.iterator()
//				.next()
//				.getPremiumPaymentId()).isEqualTo(premiumId);
//	}
//
//	protected PolicyPayment createPolicyPayment() throws Exception {
//		PolicyPayment entity = new PolicyPayment();
//		var policy = PolicyBeans.policy(funeralSchemeService, policyService);
//		entity.setPolicy(policy);
//		entity.setAmountTendered(new BigDecimal("30"));
//		entity.setPaymentDate(LocalDateTime.now());
//		entity.setPolicyPaymentDetails(PolicyPaymentServiceUnitTest.policyPaymentInfoList(policy));
//		return entity;
//	}
}
