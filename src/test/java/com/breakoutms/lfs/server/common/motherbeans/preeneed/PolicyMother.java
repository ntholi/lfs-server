package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import java.time.LocalDate;

import com.breakoutms.lfs.server.common.motherbeans.BaseMother;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

import lombok.val;

public class PolicyMother extends BaseMother<Policy> {

	public PolicyMother age(int age) {
		dateOfBirth(LocalDate.now().minusYears(age));
		return this;
	}
	
	public PolicyMother dateOfBirth(LocalDate dateOfBirth) {
		entity.setDateOfBirth(dateOfBirth);
		updatePremiumAmount();
		return this;
	}

	public PolicyMother funeralScheme(FuneralScheme funeralScheme) {
		entity.setFuneralScheme(funeralScheme);
		updatePremiumAmount();
		return this;
	}

	private void updatePremiumAmount() {
		val funeralScheme = entity.getFuneralScheme();
		int age = entity.getAgeAtRegistration();
		Premium premium = funeralScheme.getPremiums()
				.stream()
				.filter(fs -> age >= fs.getMinmumAge() && age <= fs.getMaximumAge())
				.findFirst()
				.get();
		entity.setPremiumAmount(premium.getPremiumAmount());
		entity.setCoverAmount(premium.getCoverAmount());
	}

}
