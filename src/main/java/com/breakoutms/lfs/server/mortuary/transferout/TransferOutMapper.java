package com.breakoutms.lfs.server.mortuary.transferout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.mortuary.transferout.model.TransferOut;
import com.breakoutms.lfs.server.mortuary.transferout.model.TransferOutDTO;
import com.breakoutms.lfs.server.mortuary.transferout.model.TransferOutViewModel;

@Mapper(componentModel="spring")
public abstract class TransferOutMapper {

	public static final TransferOutMapper INSTANCE = Mappers.getMapper(TransferOutMapper.class);

	@Mapping(source = "driversName",  target = "transport.driver")
	@Mapping(source = "vehicleOwner",  target = "transport.vehicle.owner")
	@Mapping(source = "registrationNumber",  target = "transport.vehicle.registrationNumber")
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	protected abstract TransferOut map(TransferOutDTO dto);
	
	@Mapping(source = "transport.driver",  target = "driversName")
	@Mapping(source = "transport.vehicle.owner",  target = "vehicleOwner")
	@Mapping(source = "transport.vehicle.registrationNumber",  target = "registrationNumber")
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	protected abstract TransferOutViewModel map(TransferOut transferOut);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "corpse", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(TransferOut updateEntity, @MappingTarget TransferOut saved);
}
