package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import java.util.Optional;

import com.breakoutms.lfs.server.common.motherbeans.AuditableMother;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit.Deductable;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.products.model.ProductType;
import com.google.common.collect.Sets;

public class FuneralSchemeMother extends AuditableMother<FuneralScheme, Integer>{

	public FuneralSchemeMother() {
		super();
		entity.getBenefits().forEach(it ->{
			it.setDiscount(money(0));
		});
	}
	
	public FuneralSchemeMother name(String name) {
		entity.setName(name);
		return this;
	}
	
	public FuneralSchemeMother nameOnly(String name) {
		entity = new FuneralScheme(name);
		return this;
	}
	
	@Override
	public FuneralSchemeMother id(Integer id) {
		entity.setId(id);
		return this;
	}

	@Override
	public FuneralSchemeMother removeIDs() {
		entity.setId(null);
		entity.getBenefits().forEach(it -> it.setId(null));
		entity.getPremiums().forEach(it -> it.setId(null));
		entity.getDependentBenefits().forEach(it -> it.setId(null));
		entity.getPenaltyDeductibles().forEach(it -> it.setId(null));
		return this;
	}
	
	public FuneralSchemeMother planC() {
		entity = FuneralScheme.builder()
					.id(7)
					.monthsBeforeActive(6)
					.monthsBeforePenalty(2)
					.name("PLAN C")
					.penaltyFee(money(10))
					.registrationFee(money(50)).build();
			entity.setPremiums(Sets.newHashSet(
					Premium.builder()
						.id(1)
						.coverAmount(money(5000))
						.maximumAge(40)
						.minmumAge(10)
						.premiumAmount(money(30))
						.funeralScheme(entity).build(),
					Premium.builder()
						.id(2)
						.coverAmount(money(5000))
						.maximumAge(65)
						.minmumAge(41)
						.premiumAmount(money(40))
						.funeralScheme(entity).build(),
					Premium.builder()
						.id(3)
						.coverAmount(money(5000))
						.maximumAge(75)
						.minmumAge(66)
						.premiumAmount(money(120))
						.funeralScheme(entity).build()
			));
			entity.setDependentBenefits(Sets.newHashSet(
					DependentBenefit.builder()
						.id(1)
						.coverAmount(money(750))
						.maximumAge(5)
						.minmumAge(0)
						.funeralScheme(entity).build(),
					DependentBenefit.builder()
						.id(2)
						.coverAmount(money(1500))
						.maximumAge(13)
						.minmumAge(6)
						.funeralScheme(entity).build(),
					DependentBenefit.builder()
						.id(3)
						.coverAmount(money(3000))
						.maximumAge(18)
						.minmumAge(14)
						.funeralScheme(entity).build()
			));
			entity.setBenefits(Sets.newHashSet(
					FuneralSchemeBenefit.builder()
						.id(1)
						.deductable(Deductable.values()[1])
						.discount(money(0))
						.productType(ProductType.values()[0])
						.funeralScheme(entity).build(),
					FuneralSchemeBenefit.builder()
						.id(2)
						.deductable(Deductable.values()[0])
						.discount(money(0))
						.productType(ProductType.values()[5])
						.funeralScheme(entity).build(),
					FuneralSchemeBenefit.builder()
						.id(3)
						.deductable(Deductable.values()[1])
						.discount(money(0))
						.productType(ProductType.values()[7])
						.funeralScheme(entity).build(),
					FuneralSchemeBenefit.builder()
						.id(4)
						.deductable(Deductable.values()[1])
						.discount(money(0))
						.productType(ProductType.values()[9])
						.funeralScheme(entity).build()
			));
			entity.setPenaltyDeductibles(Sets.newHashSet(
					PenaltyDeductible.builder()
						.id(1)
						.amount(money(1000)).months(1)
						.funeralScheme(entity).build(),
					PenaltyDeductible.builder()
						.id(2)
						.amount(money(2000)).months(2)
						.funeralScheme(entity).build(),
					PenaltyDeductible.builder()
						.id(3)
						.amount(money(3000)).months(3)
						.funeralScheme(entity).build()
			));
		setAssociations(entity);
		entity.setBranch(getBranch());
		return this;
	}
	
	public FuneralSchemeMother planAPlus() {
		entity = FuneralScheme.builder()
				.id(10)
				.includesFirstPremium(true)
				.monthsBeforeActive(6)
				.monthsBeforePenalty(2)
				.name("PLAN A+")
				.penaltyFee(money(20))
				.registrationFee(money(300)).build();
		entity.setPremiums(Sets.newHashSet(
				Premium.builder()
					.id(4)
					.coverAmount(money(15000))
					.maximumAge(40)
					.minmumAge(10)
					.premiumAmount(money(200))
					.funeralScheme(entity).build(),
				Premium.builder()
					.id(5)
					.coverAmount(money(15000))
					.maximumAge(65)
					.minmumAge(41)
					.premiumAmount(money(220))
					.funeralScheme(entity).build()
		));
		entity.setDependentBenefits(Sets.newHashSet(
				DependentBenefit.builder()
					.id(4)
					.coverAmount(money(2000))
					.maximumAge(5)
					.minmumAge(0)
					.funeralScheme(entity).build(),
				DependentBenefit.builder()
					.id(5)
					.coverAmount(money(5000))
					.maximumAge(13)
					.minmumAge(6)
					.funeralScheme(entity).build(),
				DependentBenefit.builder()
					.id(6)
					.coverAmount(money(7500))
					.maximumAge(18)
					.minmumAge(14)
					.funeralScheme(entity).build()
		));
		entity.setBenefits(Sets.newHashSet(
				FuneralSchemeBenefit.builder()
					.id(5)
					.deductable(Deductable.values()[1])
					.discount(money(0))
					.productType(ProductType.values()[0])
					.funeralScheme(entity).build(),
				FuneralSchemeBenefit.builder()
					.id(6)
					.deductable(Deductable.values()[0])
					.discount(money(0))
					.productType(ProductType.values()[5])
					.funeralScheme(entity).build(),
				FuneralSchemeBenefit.builder()
					.id(7)
					.deductable(Deductable.values()[1])
					.discount(money(0))
					.productType(ProductType.values()[7])
					.funeralScheme(entity).build(),
				FuneralSchemeBenefit.builder()
					.id(8)
					.deductable(Deductable.values()[1])
					.discount(money(0))
					.productType(ProductType.values()[9])
					.funeralScheme(entity).build()
		));
		entity.setPenaltyDeductibles(Sets.newHashSet(
				PenaltyDeductible.builder()
					.id(1)
					.amount(money(3000)).months(1)
					.funeralScheme(entity).build(),
				PenaltyDeductible.builder()
					.id(2)
					.amount(money(4000)).months(2)
					.funeralScheme(entity).build(),
				PenaltyDeductible.builder()
					.id(3)
					.amount(money(5000)).months(3)
					.funeralScheme(entity).build()
		));
		setAssociations(entity);
		entity.setBranch(getBranch());
		return this;
	}
	
	protected void setAssociations(final FuneralScheme entity) {
		if(entity.getPremiums() != null) {
			entity.getPremiums().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
		if(entity.getDependentBenefits() != null) {
			entity.getDependentBenefits().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
		if(entity.getBenefits() != null) {
			entity.getBenefits().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
		if(entity.getPenaltyDeductibles() != null) {
			entity.getPenaltyDeductibles().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
	}

	public static Optional<Premium> getPremium(FuneralScheme funeralScheme, int age) {
		if(funeralScheme != null && funeralScheme.getPremiums() != null) {
			return funeralScheme.getPremiums()
				.stream()
				.filter(fs -> age >= fs.getMinmumAge() && age <= fs.getMaximumAge())
				.findFirst();	
		}
		return Optional.empty();
	}
}
