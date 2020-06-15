package com.breakoutms.lfs.server.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.pricing.model.CoffinViewModel;
import com.breakoutms.lfs.server.products.model.Coffin;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductDTO;
import com.breakoutms.lfs.server.products.model.ProductViewModel;

@Mapper(componentModel="spring")
public abstract class ProductMapper {

	public static final ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	public abstract ProductViewModel map(Product entity);
	public abstract Product map(ProductDTO dto);
	public abstract Coffin mapCoffin(ProductDTO entity);
	public abstract CoffinViewModel mapCoffin(Coffin entity);
	protected abstract Product copy(Product product);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(Product updateEntity, @MappingTarget Product saved);
	
	protected abstract ProductDTO toDTO(Product entity);
	protected abstract ProductDTO toDTO(Coffin entity);
}
