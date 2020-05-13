package com.breakoutms.lfs.server.common.beans;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import com.breakoutms.lfs.server.preneed.PolicyService;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeService;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.preneed.pricing.utils.FuneralSchemeUtils;

import lombok.val;

public class PolicyBeans {

	public static Policy policy(FuneralSchemeService service, PolicyService policyService) throws Exception {
		val age = 43;
		FuneralScheme funeralScheme = FuneralSchemesJSON.withDependanciesButNoIds();
		Premium premium = FuneralSchemeUtils.getPremium(funeralScheme, age);
		
		service.save(funeralScheme);
		
		Policy policy = new Policy();
		policy.setNames("Thabo");
		policy.setSurname("Lebese");
		policy.setRegistrationDate(LocalDate.now());
		policy.setDateOfBirth(LocalDate.now().minusYears(age));
		policy.setFuneralScheme(funeralScheme);
		policy.setCoverAmount(premium.getCoverAmount());
		policy.setPremiumAmount(premium.getPremiumAmount());
		
		policyService.save(policy, funeralScheme.getName());
		
		return policy;
	}
}
