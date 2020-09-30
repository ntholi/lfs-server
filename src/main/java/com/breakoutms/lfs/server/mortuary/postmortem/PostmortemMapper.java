package com.breakoutms.lfs.server.mortuary.postmortem;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.postmortem.model.Postmortem;
import com.breakoutms.lfs.server.mortuary.postmortem.model.PostmortemDTO;

@Mapper(componentModel="spring")
public abstract class PostmortemMapper {

	public static final PostmortemMapper INSTANCE = Mappers.getMapper(PostmortemMapper.class);

	@Mapping(source = "driversName",  target = "transport.driver")
	@Mapping(source = "vehicleOwner",  target = "transport.vehicle.owner")
	@Mapping(source = "registrationNumber",  target = "transport.vehicle.registrationNumber")
	@Mapping(source = "returnTransportDriver",  target = "returnTransport.driver")
	@Mapping(source = "returnTransportOwner",  target = "returnTransport.vehicle.owner")
	@Mapping(source = "returnTransportRegNumber",  target = "returnTransport.vehicle.registrationNumber")
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	@Mapping(source = "requestId", target = "postmortemRequest.id")
	public abstract Postmortem map(PostmortemDTO dto);
	
	@Mapping(source = "transport.driver",  target = "driversName")
	@Mapping(source = "transport.vehicle.owner",  target = "vehicleOwner")
	@Mapping(source = "transport.vehicle.registrationNumber",  target = "registrationNumber")
	@Mapping(source = "returnTransport.driver",  target = "returnTransportDriver")
	@Mapping(source = "returnTransport.vehicle.owner",  target = "returnTransportOwner")
	@Mapping(source = "returnTransport.vehicle.registrationNumber",  target = "returnTransportRegNumber")
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	@Mapping(source = "postmortemRequest.id", target = "requestId")
	public abstract PostmortemDTO map(Postmortem entity);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	@Mapping(target = "postmortemRequest", ignore = true)
	public abstract void update(Postmortem updateEntity, @MappingTarget Postmortem saved);
}