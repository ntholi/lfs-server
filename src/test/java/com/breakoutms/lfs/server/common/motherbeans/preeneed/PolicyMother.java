package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import static org.jeasy.random.FieldPredicates.named;

import java.time.LocalDate;

import org.jeasy.random.EasyRandomParameters;

import com.breakoutms.lfs.server.common.motherbeans.AuditableMother;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

public class PolicyMother extends AuditableMother<Policy, String> {

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
		var funeralScheme = entity.getFuneralScheme();
		int age = entity.getAgeAtRegistration();
		FuneralSchemeMother.getPremium(funeralScheme, age).ifPresent(it ->{
			entity.setPremiumAmount(it.getPremiumAmount());
			entity.setCoverAmount(it.getCoverAmount());
		});
	}
	
	@Override
	protected EasyRandomParameters getRandomParameters() {
		var params = super.getRandomParameters();
		params.randomize(named("policy"), () -> null);
		return params;
	}

	public PolicyMother removeIDs() {
		entity.setPolicyNumber(null);
		entity.getDependents().forEach(it -> it.setId(null));
		return this;
	}

}
