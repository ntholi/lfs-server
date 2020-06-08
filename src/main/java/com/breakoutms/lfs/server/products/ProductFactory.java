package com.breakoutms.lfs.server.products;

import com.breakoutms.lfs.server.products.model.Coffin;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductDTO;
import com.breakoutms.lfs.server.products.model.ProductType;
import com.breakoutms.lfs.server.products.model.ProductViewModel;

public interface ProductFactory {

	public static Product get(ProductDTO dto) {
		ProductType type = dto.getProductType();
		if(type == ProductType.COFFIN_CASKET) {
			return ProductMapper.INSTANCE.mapCoffin(dto);
		}
		return ProductMapper.INSTANCE.map(dto);
	}

	public static ProductViewModel get(Product entity) {
		ProductType type = entity.getProductType();
		if(type == ProductType.COFFIN_CASKET) {
			return ProductMapper.INSTANCE.mapCoffin((Coffin)entity);
		}
		return ProductMapper.INSTANCE.map(entity);
	}
}
