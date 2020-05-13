package com.breakoutms.lfs.server.preneed.payment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.breakoutms.lfs.server.common.beans.PolicyBeans;
import com.breakoutms.lfs.server.preneed.PolicyService;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class PolicyPaymentServiceIntegrationTest {

	
	@Autowired PolicyPaymentRepository repo;
	@Autowired private PolicyPaymentService service;
	@Autowired private EntityManager entityManager;
	@Autowired private FuneralSchemeService funeralSchemeService;
	@Autowired private PolicyService policyService;
	private PolicyPayment entity;
	
	
	@BeforeEach
	void init() throws Exception {
		entity = createPolicyPayment();
	}
	
	@Test
	void test() throws Exception {
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
		var policy = PolicyBeans.policy(funeralSchemeService, policyService);
		entity.setPolicy(policy);
		entity.setAmountTendered(new BigDecimal("30"));
		entity.setPaymentDate(LocalDateTime.now());
		entity.setPolicyPaymentDetails(PolicyPaymentServiceUnitTest.policyPaymentInfoList(policy));
		return entity;
	}
}
