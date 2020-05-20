package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;

public interface PolicyPaymentDetailsRepository extends JpaRepository<PolicyPaymentDetails, Long>{

	List<PolicyPaymentDetails> findPolicyPaymentDetailsByPremiumPaymentIdIn(Set<String> premiumIds);
	
}
