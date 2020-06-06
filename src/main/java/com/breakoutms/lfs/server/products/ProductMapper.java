package com.breakoutms.lfs.server.products;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductDTO;
import com.breakoutms.lfs.server.products.model.ProductViewModel;

@Mapper(componentModel="spring")
public abstract class ProductMapper {

	public static final ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	public abstract Product map(ProductDTO dto);
	public abstract  ProductViewModel map(Product entity);
}
