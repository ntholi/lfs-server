package com.breakoutms.lfs.server.common.beans.preeneed;

import java.time.LocalDate;

import com.breakoutms.lfs.server.common.beans.Fake;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

import lombok.val;

public class PolicyFake extends Fake<Policy> {

	public PolicyFake age(int age) {
		dateOfBirth(LocalDate.now().minusYears(age));
		return this;
	}
	
	public PolicyFake dateOfBirth(LocalDate dateOfBirth) {
		entity.setDateOfBirth(dateOfBirth);
		updatePremiumAmount();
		return this;
	}

	public PolicyFake funeralScheme(FuneralScheme funeralScheme) {
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
