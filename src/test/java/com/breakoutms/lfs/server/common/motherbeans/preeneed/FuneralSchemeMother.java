package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;

import com.breakoutms.lfs.server.common.motherbeans.BaseMother;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit.Deductable;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.sales.items.ItemType;
import com.google.common.collect.Sets;

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

	public FuneralSchemeMother removeIDs() {
		entity.setId(null);
		entity.getBenefits().forEach(it -> it.setId(null));
		entity.getPremiums().forEach(it -> it.setId(null));
		entity.getDependentBenefits().forEach(it -> it.setId(null));
		entity.getPenaltyDeductibles().forEach(it -> it.setId(null));
		return this;
	}
	
	public FuneralSchemeMother ofPlanC() {
		entity = FuneralScheme.builder()
					.id(7)
					.monthsBeforeActive(6)
					.monthsBeforePenalty(2)
					.name("PLAN C")
					.penaltyFee(new BigDecimal(10))
					.registrationFee(new BigDecimal(50)).build();
			entity.setPremiums(Arrays.asList(
					Premium.builder()
						.id(1)
						.coverAmount(new BigDecimal(500))
						.maximumAge(40)
						.minmumAge(10)
						.premiumAmount(new BigDecimal(30))
						.funeralScheme(entity).build(),
					Premium.builder()
						.id(2)
						.coverAmount(new BigDecimal(500))
						.maximumAge(65)
						.minmumAge(41)
						.premiumAmount(new BigDecimal(40))
						.funeralScheme(entity).build(),
					Premium.builder()
						.id(3)
						.coverAmount(new BigDecimal(500))
						.maximumAge(75)
						.minmumAge(66)
						.premiumAmount(new BigDecimal(120))
						.funeralScheme(entity).build()
			));
			entity.setDependentBenefits(Arrays.asList(
					DependentBenefit.builder()
						.id(1)
						.coverAmount(new BigDecimal(750))
						.maximumAge(5)
						.minmumAge(0)
						.funeralScheme(entity).build(),
					DependentBenefit.builder()
						.id(2)
						.coverAmount(new BigDecimal(1500))
						.maximumAge(13)
						.minmumAge(6)
						.funeralScheme(entity).build(),
					DependentBenefit.builder()
						.id(3)
						.coverAmount(new BigDecimal(3000))
						.maximumAge(18)
						.minmumAge(14)
						.funeralScheme(entity).build()
			));
			entity.setBenefits(Arrays.asList(
					FuneralSchemeBenefit.builder()
						.id(1)
						.deductable(Deductable.values()[1])
						.discount(new BigDecimal(0))
						.itemType(ItemType.values()[0])
						.funeralScheme(entity).build(),
					FuneralSchemeBenefit.builder()
						.id(2)
						.deductable(Deductable.values()[0])
						.discount(new BigDecimal(0))
						.itemType(ItemType.values()[5])
						.funeralScheme(entity).build(),
					FuneralSchemeBenefit.builder()
						.id(3)
						.deductable(Deductable.values()[1])
						.discount(new BigDecimal(0))
						.itemType(ItemType.values()[7])
						.funeralScheme(entity).build(),
					FuneralSchemeBenefit.builder()
						.id(4)
						.deductable(Deductable.values()[1])
						.discount(new BigDecimal(0))
						.itemType(ItemType.values()[9])
						.funeralScheme(entity).build()
			));
			entity.setPenaltyDeductibles(Arrays.asList(
					PenaltyDeductible.builder()
						.id(1)
						.amount(new BigDecimal(1000)).months(1)
						.funeralScheme(entity).build(),
					PenaltyDeductible.builder()
						.id(2)
						.amount(new BigDecimal(2000)).months(2)
						.funeralScheme(entity).build(),
					PenaltyDeductible.builder()
						.id(3)
						.amount(new BigDecimal(3000)).months(3)
						.funeralScheme(entity).build()
			));
		return this;
	}
}
