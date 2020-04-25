package com.breakoutms.lfs.server.preneed;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.preneed.pricing.model.PremiumViewModel;

@Mapper(componentModel="spring")
public abstract class PreneedMapper {

	public static final PreneedMapper INSTANCE = Mappers.getMapper(PreneedMapper.class);
	
	public abstract FuneralScheme map(FuneralSchemeDTO funeralScheme);
	public abstract FuneralSchemeViewModel map(FuneralScheme funeralScheme);
	public abstract PremiumViewModel map(Premium premium);
	
	public abstract PolicyDTO map(Policy policy);
}
