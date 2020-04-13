package lfs.server.core;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import lfs.server.mortuary.Corpse;
import lfs.server.mortuary.CorpseResponseDTO;
import lfs.server.preneed.Policy;
import lfs.server.preneed.PolicyDTO;
import lfs.server.preneed.pricing.FuneralScheme;
import lfs.server.preneed.pricing.FuneralSchemeDTO;

@Mapper(componentModel="spring")
public abstract class DtoMapper {
	
	public static final DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);
	
	public abstract CorpseResponseDTO map(Corpse corpse);
	public abstract FuneralSchemeDTO map(FuneralScheme funeralScheme);
	public abstract PolicyDTO map(Policy policy);
}
