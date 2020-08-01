package com.breakoutms.lfs.server.mortuary.released;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpse;
import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpseDTO;
import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpseViewModel;

@Mapper(componentModel="spring")
public abstract class ReleasedCorpseMapper {

	public static final ReleasedCorpseMapper INSTANCE = Mappers.getMapper(ReleasedCorpseMapper.class);

	@Mapping(source = "tagNo", target = "corpse.tagNo")
	protected abstract ReleasedCorpse map(ReleasedCorpseDTO dto);
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	protected abstract ReleasedCorpseViewModel map(ReleasedCorpse releasedCorpse);
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	protected abstract ReleasedCorpseDTO toDTO(ReleasedCorpse entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "corpse", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(ReleasedCorpse updateEntity, @MappingTarget ReleasedCorpse saved);
}
