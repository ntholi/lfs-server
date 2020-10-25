package com.breakoutms.lfs.server.reception.embalming;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.reception.embalming.model.EmbalmingRequest;
import com.breakoutms.lfs.server.reception.embalming.model.EmbalmingRequestDTO;
import com.breakoutms.lfs.server.reception.embalming.utils.BooleanToString;

@Mapper(componentModel="spring")
public abstract class EmbalmingRequestMapper {

	public static final EmbalmingRequestMapper INSTANCE = Mappers.getMapper(EmbalmingRequestMapper.class);
	
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	public abstract EmbalmingRequest map(EmbalmingRequestDTO dto);
	
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	public abstract EmbalmingRequestDTO map(EmbalmingRequest entity);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	public abstract void update(EmbalmingRequest updatedEntity, @MappingTarget EmbalmingRequest entity);
	
    public String booleanToString(final Boolean bool) {
    	return BooleanToString.booleanToString(bool);
    }
    
    public Boolean stringToBoolean(final String str) {
    	return BooleanToString.stringToBoolean(str);
    }
}
