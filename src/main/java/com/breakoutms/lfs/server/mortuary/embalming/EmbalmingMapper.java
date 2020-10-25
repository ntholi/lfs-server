package com.breakoutms.lfs.server.mortuary.embalming;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.embalming.model.Embalming;
import com.breakoutms.lfs.server.mortuary.embalming.model.EmbalmingDTO;

@Mapper(componentModel="spring")
public abstract class EmbalmingMapper {

	public static final EmbalmingMapper INSTANCE = Mappers.getMapper(EmbalmingMapper.class);

	@Mapping(source = "tagNo", target = "corpse.tagNo")
	@Mapping(source = "requestId", target = "embalmingRequest.id")
	protected abstract Embalming map(EmbalmingDTO dto);
	
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	@Mapping(source = "embalmingRequest.id", target = "requestId")
	protected abstract EmbalmingDTO map(Embalming embalmingCorpse);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "corpse", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Embalming updateEntity, @MappingTarget Embalming saved);
}
