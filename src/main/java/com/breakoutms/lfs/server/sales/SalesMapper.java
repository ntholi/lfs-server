package com.breakoutms.lfs.server.sales;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesDTO;
import com.breakoutms.lfs.server.sales.model.SalesEagerResponse;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.sales.model.SalesProductDTO;
import com.breakoutms.lfs.server.sales.model.SalesProductViewModel;
import com.breakoutms.lfs.server.sales.model.SalesViewModel;

@Mapper(componentModel="spring")
public abstract class SalesMapper {

	public static final SalesMapper INSTANCE = Mappers.getMapper(SalesMapper.class);

	@Mapping(source = "customerNames", target = "quotation.customer.names")
	@Mapping(source = "phoneNumber", target = "quotation.customer.phoneNumber")
	@Mapping(source = "salesProducts", target = "quotation.salesProducts")
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	@Mapping(source = "leavingTime", target = "burialDetails.leavingTime")
	@Mapping(source = "serviceTime", target = "burialDetails.serviceTime")
	@Mapping(source = "burialPlace", target = "burialDetails.burialPlace")
	@Mapping(source = "roadStatus", target = "burialDetails.roadStatus")
	@Mapping(source = "physicalAddress", target = "burialDetails.physicalAddress")
	protected abstract Sales map(SalesDTO dto);

	@Mapping(source = "quotation.id", target = "quotationNo")
	@Mapping(source = "quotation.customer.names", target = "customerNames")
	@Mapping(source = "quotation.customer.phoneNumber", target = "phoneNumber")
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	@Mapping(source = "burialDetails.leavingTime", target = "leavingTime")
	@Mapping(source = "burialDetails.serviceTime", target = "serviceTime")
	@Mapping(source = "burialDetails.burialPlace", target = "burialPlace")
	@Mapping(source = "burialDetails.roadStatus", target = "roadStatus")
	@Mapping(source = "burialDetails.physicalAddress", target = "physicalAddress")
	protected abstract SalesViewModel map(Sales sales);
	
	@Mapping(source = "quotation.id", target = "quotationNo")
	@Mapping(source = "quotation.customer.names", target = "customerNames")
	@Mapping(source = "quotation.customer.phoneNumber", target = "phoneNumber")
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	@Mapping(source = "burialDetails.leavingTime", target = "leavingTime")
	@Mapping(source = "burialDetails.serviceTime", target = "serviceTime")
	@Mapping(source = "burialDetails.burialPlace", target = "burialPlace")
	@Mapping(source = "burialDetails.roadStatus", target = "roadStatus")
	@Mapping(source = "burialDetails.physicalAddress", target = "physicalAddress")
	@Mapping(source = "quotation.salesProducts", target = "salesProducts")
	public abstract SalesEagerResponse eager(Sales entity);

	@Mapping(source = "quotation.customer.names", target = "customerNames")
	@Mapping(source = "quotation.customer.phoneNumber", target = "phoneNumber")
	@Mapping(source = "quotation.salesProducts", target = "salesProducts")
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	@Mapping(source = "burialDetails.leavingTime", target = "leavingTime")
	@Mapping(source = "burialDetails.serviceTime", target = "serviceTime")
	@Mapping(source = "burialDetails.burialPlace", target = "burialPlace")
	@Mapping(source = "burialDetails.roadStatus", target = "roadStatus")
	@Mapping(source = "burialDetails.physicalAddress", target = "physicalAddress")
	protected abstract SalesDTO toDTO(Sales entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "quotation.id", ignore = true)
	@Mapping(target = "quotation.customer.id", ignore = true)
	@Mapping(target = "burialDetails.id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Sales updateEntity, @MappingTarget Sales saved);
	
	protected abstract Sales copy(Sales sales);

	protected abstract List<SalesProductViewModel> map(List<SalesProduct> salesProducts);
	
	@Mapping(source = "product.name", target = "productName")
	@Mapping(source = "product.id", target = "productId")
	@Mapping(source = "product.price", target = "unitPrice")
	protected abstract SalesProductViewModel map(SalesProduct salesProduct);
	
	@Mapping(source = "productId", target = "product.id")
	protected abstract SalesProduct map(SalesProductDTO dto);
}
