package com.breakoutms.lfs.server.preneed.policy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.breakoutms.lfs.server.preneed.policy.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, String>{

	@Query("SELECT new "
			+ "com.breakoutms.lfs.server.preneed.policy.model.Policy(p.policyNumber, p.registrationDate, p.status) "
			+ "FROM Policy as p "
			+ "where p.policyNumber = :policyNumber")
	Optional<Policy> getPolicyStatus(String policyNumber);

}
