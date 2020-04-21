package com.breakoutms.lfs.server.core;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.Corpse;
import com.breakoutms.lfs.server.mortuary.CorpseResponseDTO;
import com.breakoutms.lfs.server.preneed.Policy;
import com.breakoutms.lfs.server.preneed.PolicyDTO;
import com.breakoutms.lfs.server.preneed.pricing.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeDTO;
import com.breakoutms.lfs.server.user.User;
import com.breakoutms.lfs.server.user.dto.UserDto;

@Mapper(componentModel="spring")
public abstract class DtoMapper {
	
	public static final DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);
	
	public abstract CorpseResponseDTO map(Corpse corpse);
	public abstract FuneralSchemeDTO map(FuneralScheme funeralScheme);
	public abstract PolicyDTO map(Policy policy);
	public abstract User map(UserDto userDto);
}
