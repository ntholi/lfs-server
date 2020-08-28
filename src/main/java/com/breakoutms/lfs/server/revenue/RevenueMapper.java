package com.breakoutms.lfs.server.revenue;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.revenue.model.RevenueDTO;
import com.breakoutms.lfs.server.revenue.model.RevenueEagerResponse;
import com.breakoutms.lfs.server.revenue.model.RevenueViewModel;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.sales.model.SalesProductViewModel;

@Mapper(componentModel="spring")
public abstract class RevenueMapper {

	public static final RevenueMapper INSTANCE = Mappers.getMapper(RevenueMapper.class);

	@Mapping(source = "customerNames", target = "quotation.customer.names")
	@Mapping(source = "phoneNumber", target = "quotation.customer.phoneNumber")
	@Mapping(source = "revenueProducts", target = "quotation.revenueProducts")
	@Mapping(source = "tagNo", target = "burialDetails.corpse.tagNo")
	@Mapping(source = "leavingTime", target = "burialDetails.leavingTime")
	@Mapping(source = "serviceTime", target = "burialDetails.serviceTime")
	@Mapping(source = "burialPlace", target = "burialDetails.burialPlace")
	@Mapping(source = "roadStatus", target = "burialDetails.roadStatus")
	@Mapping(source = "physicalAddress", target = "burialDetails.physicalAddress")
	protected abstract Revenue map(RevenueDTO dto);

	@Mapping(source = "quotation.id", target = "quotationNo")
	@Mapping(source = "quotation.customer.names", target = "customerNames")
	@Mapping(source = "quotation.customer.phoneNumber", target = "phoneNumber")
	@Mapping(source = "burialDetails.corpse.tagNo", target = "tagNo")
	@Mapping(source = "burialDetails.leavingTime", target = "leavingTime")
	@Mapping(source = "burialDetails.serviceTime", target = "serviceTime")
	@Mapping(source = "burialDetails.burialPlace", target = "burialPlace")
	@Mapping(source = "burialDetails.roadStatus", target = "roadStatus")
	@Mapping(source = "burialDetails.physicalAddress", target = "physicalAddress")
	protected abstract RevenueViewModel map(Revenue revenue);
	
	@Mapping(source = "quotation.id", target = "quotationNo")
	@Mapping(source = "quotation.customer.names", target = "customerNames")
	@Mapping(source = "quotation.customer.phoneNumber", target = "phoneNumber")
	@Mapping(source = "burialDetails.corpse.tagNo", target = "tagNo")
	@Mapping(source = "burialDetails.leavingTime", target = "leavingTime")
	@Mapping(source = "burialDetails.serviceTime", target = "serviceTime")
	@Mapping(source = "burialDetails.burialPlace", target = "burialPlace")
	@Mapping(source = "burialDetails.roadStatus", target = "roadStatus")
	@Mapping(source = "burialDetails.physicalAddress", target = "physicalAddress")
	@Mapping(source = "quotation.revenueProducts", target = "revenueProducts")
	public abstract RevenueEagerResponse eager(Revenue entity);

	@Mapping(source = "quotation.customer.names", target = "customerNames")
	@Mapping(source = "quotation.customer.phoneNumber", target = "phoneNumber")
	@Mapping(source = "quotation.revenueProducts", target = "revenueProducts")
	@Mapping(source = "burialDetails.corpse.tagNo", target = "tagNo")
	@Mapping(source = "burialDetails.leavingTime", target = "leavingTime")
	@Mapping(source = "burialDetails.serviceTime", target = "serviceTime")
	@Mapping(source = "burialDetails.burialPlace", target = "burialPlace")
	@Mapping(source = "burialDetails.roadStatus", target = "roadStatus")
	@Mapping(source = "burialDetails.physicalAddress", target = "physicalAddress")
	protected abstract RevenueDTO toDTO(Revenue entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "quotation.id", ignore = true)
	@Mapping(target = "quotation.customer.id", ignore = true)
	@Mapping(target = "burialDetails.id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Revenue updateEntity, @MappingTarget Revenue saved);
	
	protected abstract Revenue copy(Revenue revenue);

	protected abstract List<SalesProductViewModel> map(List<SalesProduct> revenueProducts);
	
	@Mapping(source = "product.name", target = "productName")
	protected abstract SalesProductViewModel map(SalesProduct revenueProduct);
}
