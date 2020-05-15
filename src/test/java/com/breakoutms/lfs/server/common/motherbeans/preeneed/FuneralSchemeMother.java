package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import java.math.BigDecimal;

import com.breakoutms.lfs.server.common.motherbeans.BaseMother;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

public class FuneralSchemeMother extends BaseMother<FuneralScheme>{

	public FuneralSchemeMother() {
		entity.getBenefits().forEach(it ->{
			it.setDiscount(new BigDecimal("0.0"));
		});
	}
	
	public FuneralSchemeMother name(String name) {
		entity.setName(name);
		return this;
	}

	public FuneralSchemeMother id(Integer id) {
		entity.setId(id);
		return this;
	}

	public BaseMother<FuneralScheme> noIds() {
		entity.setId(null);
		entity.getBenefits().forEach(it -> it.setId(null));
		entity.getPremiums().forEach(it -> it.setId(null));
		entity.getDependentBenefits().forEach(it -> it.setId(null));
		entity.getPenaltyDeductibles().forEach(it -> it.setId(null));
		return this;
	}
}
