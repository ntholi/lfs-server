package com.breakoutms.lfs.server.preneed;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefitDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefitDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductibleDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.preneed.pricing.model.PremiumDTO;

@Mapper(componentModel="spring")
public abstract class PreneedMapper {

	public static final PreneedMapper INSTANCE = Mappers.getMapper(PreneedMapper.class);
	
	public abstract FuneralScheme map(FuneralSchemeDTO funeralScheme);
	public abstract FuneralSchemeDTO map(FuneralScheme funeralScheme);
	public abstract PremiumDTO map(Premium premium);
	public abstract PenaltyDeductibleDTO map(PenaltyDeductible penaltyDeductible);
	public abstract FuneralSchemeBenefitDTO map(FuneralSchemeBenefit funeralSchemeBenefit);
	public abstract DependentBenefitDTO map(DependentBenefit dependentBenefit);
	public abstract FuneralScheme copy(FuneralScheme entity);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	public abstract void update(FuneralScheme updatedEntity, @MappingTarget FuneralScheme saved);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	@Mapping(target = "age", ignore = true)
	public abstract void update(Policy updatedEntity, @MappingTarget Policy saved);
	
	@Mapping(target = "funeralScheme", ignore = true)
	public abstract Policy map(PolicyDTO dto);
	
	@Mapping(source = "funeralScheme.name", target = "funeralScheme")
	public abstract PolicyDTO map(Policy entity);
}
