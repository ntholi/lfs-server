package com.breakoutms.lfs.server.common.beans;

import org.junit.jupiter.api.Test;

import com.breakoutms.lfs.server.common.beans.preeneed.PolicyFake;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;

class TestFakes {

	@Test
	void test() throws Exception {
		Policy policy = new PolicyFake()
				.age(43)
				.funeralScheme(FuneralSchemesJSON.byName("PLAN C"))
				.build();
		System.out.println(policy.getCoverAmount());
	}

}
