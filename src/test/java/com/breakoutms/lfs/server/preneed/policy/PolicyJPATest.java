package com.breakoutms.lfs.server.preneed.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DBUnit(allowEmptyFields = true) 
@DataSet({"policy.xml"})
public class PolicyJPATest {

	@Autowired PolicyRepository repo;
	
	@Test
	void test_getPolicyStatus() {
		String policyNumber = "256070796";
		
		Policy policy = repo.getPolicyStatus(policyNumber).get();
		
		assertThat(policy.getId()).isEqualTo(policyNumber);
		assertThat(policy.getStatus()).isEqualTo(policy.getStatus());
		assertThat(policy.getRegistrationDate()).isEqualTo(policy.getRegistrationDate());
		assertThat(policy.getNames()).isNull();
		assertThat(policy.getPremiumAmount()).isNull();
	}
}
