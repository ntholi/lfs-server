package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;

@Repository
public interface PolicyPaymentRepository extends JpaRepository<PolicyPayment, Long>{

	@Query("FROM PolicyPaymentDetails e WHERE e.policyPayment.id = :id AND e.deleted=false")
	List<PolicyPaymentDetails> getPaymentDetails(Long id);

	@Query("SELECT period FROM PolicyPaymentDetails ")
//			+ "WHERE policyPayment.policy = :policy "
//			+ "AND policyPayment.type = 'PREMIUM' "
//			+ "AND policyPayment.deleted=false "
//			+ "ORDER BY year desc, month desc") 
//	//TODO: LIMIT THE RESULTS TO RETURN ONLY ONE RECORD
	Optional<Period> getLastPayedPeriod(Policy policy);

	@Query("FROM UnpaidPolicyPayment e WHERE e.policy.policyNumber = :policyNumber AND e.deleted=false")
	List<UnpaidPolicyPayment> getUnpaidPolicyPayment(String policyNumber);
}
