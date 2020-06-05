package com.breakoutms.lfs.server.preneed.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;

public interface PolicyPaymentDetailsRepository extends JpaRepository<PolicyPaymentDetails, Long>{

	
	/**
	 * See @NamedNativeQuery query in {@link PolicyPaymentDetails}
	 * @param policyNumber
	 * @return
	 */
	//TODO: FOR SOME REASON, THIS METHOD UPDATES POLICY SOMEHOW, i DON'T KNOW HOW
	@Query(nativeQuery = true)
	Optional<Period> getLastPayedPeriod(@Param("policyNumber") String policyNumber);
}
