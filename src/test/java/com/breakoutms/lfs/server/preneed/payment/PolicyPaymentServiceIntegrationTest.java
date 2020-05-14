package com.breakoutms.lfs.server.preneed.payment;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DataSet("branch.xml")
public class PolicyPaymentServiceIntegrationTest {


	@Autowired BranchRepository branchRepo;
	@Autowired FuneralSchemeRepository funeralSchemeRepo;

	@Test
	@DataSet({"funeral_scheme.xml"})
	void test() {
		
		branchRepo.findAll().spliterator().forEachRemaining(it ->{
			System.err.println("xxxxxxxxxxxxxxxxxxx: "+it.getName());
//			System.err.println(it.getPenaltyDeductibles());
		});
	}
	
	
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
