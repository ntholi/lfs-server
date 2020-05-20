package com.breakoutms.lfs.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.preneed.PolicyRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DBUnit(allowEmptyFields = true) 
@DataSet({"funeral_scheme.xml", "policy.xml"})
public class RandomTests {
	
	
	@Autowired FuneralSchemeRepository funeralSchemeRepo;
	@Autowired PolicyRepository policyRepo;
	
	@Test
	void test() {
		policyRepo.findAll().forEach(it ->{
			System.out.println(it.getNames());
		});
		
	}
}
