package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.core.AuditableRepository;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;

@Repository
public interface PolicyPaymentRepository extends AuditableRepository<PolicyPayment, Long>{

	@Query("FROM PolicyPaymentDetails e WHERE e.policyPayment.id = :id AND e.deleted=false")
	List<PolicyPaymentDetails> getPaymentDetails(Long id);

	@Query("SELECT period FROM PolicyPaymentDetails "
			+ "WHERE policyPayment.policy = :policy "
			+ "AND type = :type order by year desc, month desc "
			+ "AND policyPayment.deleted=false") //TODO: LIMIT THE RESULTS TO RETURN ONLY ONE RECORD
	Period getLastPayedPeriod(String policyNumber);

	@Query("FROM UnpaidPolicyPayment e WHERE e.policy.policyNumber = :policyNumber AND e.deleted=false")
	List<UnpaidPolicyPayment> getUnpaidPolicyPayment(String policyNumber);
}
