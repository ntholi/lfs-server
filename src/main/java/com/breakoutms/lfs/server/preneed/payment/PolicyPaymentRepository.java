package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.core.AuditableRepository;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;

@Repository
public interface PolicyPaymentRepository extends AuditableRepository<PolicyPayment, Long>{

	@Query("FROM PolicyPaymentDetails e WHERE e.policyPayment.id = :id AND e.deleted=false")
	List<PolicyPaymentDetails> getPaymentDetails(Long id);
}
