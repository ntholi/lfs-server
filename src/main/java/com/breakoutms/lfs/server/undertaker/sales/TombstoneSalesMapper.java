package com.breakoutms.lfs.server.undertaker.sales;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.undertaker.sales.model.TombstoneSales;
import com.breakoutms.lfs.server.undertaker.sales.model.TombstoneSalesDTO;

@Mapper(componentModel="spring")
public abstract class TombstoneSalesMapper {

	public static final TombstoneSalesMapper INSTANCE = Mappers.getMapper(TombstoneSalesMapper.class);
	
	@Mapping(source = "salesProducts", target = "quotation.salesProducts")
	@Mapping(source = "quotationNo", target = "quotation.id")
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	protected abstract TombstoneSales map(TombstoneSalesDTO dto);

	@Mapping(source = "quotation.id", target = "quotationNo")
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	@Mapping(source = "quotation.salesProducts", target = "salesProducts")
	protected abstract TombstoneSalesDTO map(TombstoneSales sales);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "quotation.id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(TombstoneSales updateEntity, @MappingTarget TombstoneSales saved);
}
