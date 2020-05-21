package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.preneed.policy.model.Policy;
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
	
	@Query(value = "SELECT premium_payment_id FROM policy_payment_details "
			+ "WHERE policy_number = :policyNumber "
			+ "ORDER BY premium_payment_id DESC "
			+ "LIMIT 1", nativeQuery = true)
	Optional<String> findLastPremiumId(String policyNumber);

	@Query("select period FROM PolicyPaymentDetails d WHERE d.premiumPaymentId IN :premiumIds")
	List<Period> findPeriodsByPaymentIds(Set<String> premiumIds);
}
