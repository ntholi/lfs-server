package com.breakoutms.lfs.server.preneed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.breakoutms.lfs.server.preneed.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, String>{

	@Query("SELECT new "
			+ "com.breakoutms.lfs.server.preneed.model.Policy(p.policyNumber, p.status) "
			+ "FROM Policy as p "
			+ "where p.policyNumber = :policyNumber")
	Policy getPolicyStatus(String policyNumber);

}
