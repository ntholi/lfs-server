package com.breakoutms.lfs.server.common.motherbeans;

import org.junit.jupiter.api.Test;

import com.breakoutms.lfs.server.common.motherbeans.preeneed.FuneralSchemeMother;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;

import lombok.val;

class TestMothers {

	
	@Test
	void funeralSchemeFake() {
		val fake = new FuneralSchemeMother()
				.id(12)
				.name("Hello World")
				.build();
		System.out.println("Id: "+ fake.getId());
	}
	
	@Test
	void policyFake() throws Exception {
		Policy policy = new PolicyMother()
				.age(43)
				.funeralScheme(FuneralSchemesJSON.byName("PLAN C"))
				.build();
		System.out.println(policy.getCoverAmount());
	}

}
