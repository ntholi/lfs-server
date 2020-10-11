package com.breakoutms.lfs.server.mortuary.corpse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseDTO;
import com.breakoutms.lfs.server.mortuary.corpse.model.OtherMortuary;

@Mapper(componentModel="spring")
public abstract class CorpseMapper {

	public static final CorpseMapper INSTANCE = Mappers.getMapper(CorpseMapper.class);

	protected OtherMortuary map(String name) {
		if(name != null && !name.isBlank()) {
			return new OtherMortuary(name);
		}
		return null;
	}
	
	protected String map(OtherMortuary entity) {
		if(entity != null) {
			return entity.getName();
		}
		return null;
	}
	
	@Mapping(target = "driversName",  source = "transport.driver")
	@Mapping(target = "vehicleOwner",  source = "transport.vehicle.owner")
	@Mapping(target = "registrationNumber",  source = "transport.vehicle.registrationNumber")
	protected abstract CorpseDTO map(Corpse entity);
	
	@Mapping(target = "transport.driver",  source = "driversName")
	@Mapping(target = "transport.vehicle.owner",  source = "vehicleOwner")
	@Mapping(target = "transport.vehicle.registrationNumber",  source = "registrationNumber")
	public abstract Corpse map(CorpseDTO dto);
	
	protected abstract Corpse copy(Corpse product);
	
	@Mapping(target = "tagNo", ignore = true)
	@Mapping(target = "transport.id", ignore = true)
	@Mapping(target = "releasedCorpse.id", ignore = true)
	@Mapping(target = "transferredFrom.id", ignore = true)
	@Mapping(target = "quotation", ignore = true)
	@Mapping(target = "policy", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Corpse updateEntity, @MappingTarget Corpse saved);
}
