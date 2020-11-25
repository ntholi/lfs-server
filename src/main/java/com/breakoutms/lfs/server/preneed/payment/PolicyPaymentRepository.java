package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;

import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

@Repository
public interface PolicyPaymentRepository extends JpaRepository<PolicyPayment, Long>, JpaSpecificationExecutorWithProjection<PolicyPayment>{

	@Query("FROM PolicyPaymentDetails e WHERE e.policyPayment.id = :id AND e.deleted=false")
	List<PolicyPaymentDetails> getPaymentDetails(Long id);
	
	@Query(value = "SELECT premium_id FROM policy_payment_details "
			+ "WHERE policy = :policy "
			+ "ORDER BY premium_id DESC "
			+ "LIMIT 1", nativeQuery = true)
	Optional<String> findLastPremiumId(Policy policy);

	@Query("select period FROM PolicyPaymentDetails d WHERE d.premiumId IN :premiumIds")
	List<Period> findPeriodsByPremiumIds(Set<String> premiumIds);
}
