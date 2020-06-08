package com.breakoutms.lfs.server.sales;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesDTO;
import com.breakoutms.lfs.server.sales.model.SalesViewModel;

@Mapper(componentModel="spring")
public abstract class SalesMapper {

	public static final SalesMapper INSTANCE = Mappers.getMapper(SalesMapper.class);

	@Mapping(source = "customerNames", target = "quotation.customer.names")
	@Mapping(source = "phoneNumber", target = "quotation.customer.phoneNumber")
	@Mapping(source = "salesProducts", target = "quotation.salesProducts")
	@Mapping(source = "tagNo", target = "burialDetails.corpse.tagNo")
	@Mapping(source = "leavingTime", target = "burialDetails.leavingTime")
	@Mapping(source = "serviceTime", target = "burialDetails.serviceTime")
	@Mapping(source = "burialPlace", target = "burialDetails.burialPlace")
	@Mapping(source = "roadStatus", target = "burialDetails.roadStatus")
	@Mapping(source = "physicalAddress", target = "burialDetails.physicalAddress")
	protected abstract Sales map(SalesDTO dto);

	@Mapping(source = "quotation.id", target = "quotationNo")
	@Mapping(source = "quotation.customer.names", target = "customerNames")
	@Mapping(source = "quotation.customer.phoneNumber", target = "phoneNumber")
	@Mapping(source = "burialDetails.corpse.tagNo", target = "tagNo")
	@Mapping(source = "burialDetails.leavingTime", target = "leavingTime")
	@Mapping(source = "burialDetails.serviceTime", target = "serviceTime")
	@Mapping(source = "burialDetails.burialPlace", target = "burialPlace")
	@Mapping(source = "burialDetails.roadStatus", target = "roadStatus")
	@Mapping(source = "burialDetails.physicalAddress", target = "physicalAddress")
	protected abstract SalesViewModel map(Sales sales);
}
