package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;

public interface UnpaidPolicyPaymentRepository extends JpaRepository<UnpaidPolicyPayment, Long>{

	List<UnpaidPolicyPayment> findByPolicy(Policy policy);
	
}
