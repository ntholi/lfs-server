package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;
import java.util.Set;

import org.springframework.data.util.Streamable;

import com.breakoutms.lfs.server.core.AuditableRepository;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;

public interface PolicyPaymentDetailsRepository extends AuditableRepository<PolicyPaymentDetails, Long>{

	List<PolicyPaymentDetails> findPolicyPaymentDetailsByPremiumPaymentIdIn(Set<String> premiumIds);
	
}
