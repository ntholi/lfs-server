package com.breakoutms.lfs.server.mortuary;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.model.Corpse;
import com.breakoutms.lfs.server.mortuary.model.CorpseDTO;
import com.breakoutms.lfs.server.mortuary.model.CorpseViewModel;
import com.breakoutms.lfs.server.mortuary.model.OtherMortuary;

@Mapper(componentModel="spring")
public abstract class CorpseMapper {

	public static final CorpseMapper INSTANCE = Mappers.getMapper(CorpseMapper.class);

	public OtherMortuary map(String name) {
		if(name != null && !name.isBlank()) {
			return new OtherMortuary(name);
		}
		return null;
	}
	
	public String map(OtherMortuary entity) {
		if(entity != null) {
			return entity.getName();
		}
		return null;
	}
	
	public abstract CorpseViewModel map(Corpse entity);
	public abstract Corpse map(CorpseDTO dto);
	protected abstract Corpse copy(Corpse product);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Corpse updateEntity, @MappingTarget Corpse saved);
	
	protected abstract CorpseDTO toDTO(Corpse entity);
}
