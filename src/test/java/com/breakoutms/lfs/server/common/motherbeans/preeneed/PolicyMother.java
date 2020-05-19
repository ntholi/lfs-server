package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import static org.jeasy.random.FieldPredicates.named;

import java.time.LocalDate;

import org.jeasy.random.EasyRandomParameters;

import com.breakoutms.lfs.server.common.motherbeans.AuditableMother;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

public class PolicyMother extends AuditableMother<Policy, String> {

	public enum PlanType {
		Plan_C,
		Plan_A_Plus
	}
	
	public static Policy of(PlanType planType, int age) {
		FuneralSchemeMother fs = new FuneralSchemeMother();
		if(planType == PlanType.Plan_C) {
			fs = fs.planC();
		}
		else fs = fs.planAPlus();
		
		return new PolicyMother()
				.funeralScheme(fs.build())
				.age(age)
				.build();
	}
	
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
	
	public PolicyMother removeIDs() {
		entity.setPolicyNumber(null);
		entity.getDependents().forEach(it -> it.setId(null));
		return this;
	}
	
	@Override
	protected EasyRandomParameters getRandomParameters() {
		var params = super.getRandomParameters();
		params.randomize(named("policy"), () -> null);
		return params;
	}
}
