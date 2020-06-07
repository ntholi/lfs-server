package com.breakoutms.lfs.server.products;

import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductDTO;
import com.breakoutms.lfs.server.products.model.ProductType;

public interface ProductFactory {

	public static Product get(ProductDTO dto) {
		ProductType type = dto.getProductType();
		if(type == ProductType.COFFIN_CASKET) {
			return ProductMapper.INSTANCE.toCoffin(dto);
		}
		return ProductMapper.INSTANCE.map(dto);
	}
}
