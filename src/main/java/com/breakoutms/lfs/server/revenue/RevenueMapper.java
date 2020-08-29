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

	@Mapping(source = "quotationNo", target = "quotation.id")
	protected abstract Revenue map(RevenueDTO dto);

	@Mapping(source = "quotation.id", target = "quotationNo")
	protected abstract RevenueViewModel map(Revenue revenue);
	
	@Mapping(source = "quotation.id", target = "quotationNo")
	@Mapping(source = "quotation.salesProducts", target = "salesProducts")
	public abstract RevenueEagerResponse eager(Revenue entity);

	@Mapping(source = "quotation.id", target = "quotationNo")
	protected abstract RevenueDTO toDTO(Revenue entity);

	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Revenue updateEntity, @MappingTarget Revenue saved);
	
	protected abstract Revenue copy(Revenue revenue);

	protected abstract List<SalesProductViewModel> map(List<SalesProduct> revenueProducts);
	
	@Mapping(source = "product.name", target = "productName")
	protected abstract SalesProductViewModel map(SalesProduct revenueProduct);
}
