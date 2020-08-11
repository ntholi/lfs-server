package com.breakoutms.lfs.server.mortuary.embalming;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.embalming.model.Embalming;
import com.breakoutms.lfs.server.mortuary.embalming.model.EmbalmingDTO;
import com.breakoutms.lfs.server.mortuary.embalming.model.EmbalmingViewModel;

@Mapper(componentModel="spring")
public abstract class EmbalmingMapper {

	public static final EmbalmingMapper INSTANCE = Mappers.getMapper(EmbalmingMapper.class);

	@Mapping(source = "tagNo", target = "corpse.tagNo")
	protected abstract Embalming map(EmbalmingDTO dto);
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	protected abstract EmbalmingViewModel map(Embalming embalmingCorpse);
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	protected abstract EmbalmingDTO toDTO(Embalming entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "corpse", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Embalming updateEntity, @MappingTarget Embalming saved);
}
