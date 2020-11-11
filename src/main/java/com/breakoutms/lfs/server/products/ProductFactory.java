package com.breakoutms.lfs.server.products;

import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.server.products.model.Coffin;
import com.breakoutms.lfs.server.products.model.EmbalmingPrice;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductDTO;
import com.breakoutms.lfs.server.products.model.ProductViewModel;
import com.breakoutms.lfs.server.products.model.Tombstone;
import com.breakoutms.lfs.server.products.model.TransportPrice;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class ProductFactory {

	private ProductFactory() {}
	
	public static Product get(ProductDTO dto) {
		ProductType type = dto.getProductType();
		try {
			if (type == ProductType.COFFIN_CASKET) {
				return ProductMapper.INSTANCE.mapCoffin(dto);
			} else if (type == ProductType.TOMBSTONE) {
				return ProductMapper.INSTANCE.mapTombstone(dto);
			} else if (type == ProductType.TRANSPORT) {
				return ProductMapper.INSTANCE.mapTransport(dto);
			} else if (type == ProductType.MORTUARY) {
				return ProductMapper.INSTANCE.mapEmbalmingPrice(dto);
			} 
		} catch (Exception ex) {
			log.error(ex);
		}
		return ProductMapper.INSTANCE.map(dto);
	}

	public static ProductViewModel get(Product entity) {
		ProductType type = entity.getProductType();
		try {
			if (type == ProductType.COFFIN_CASKET) {
				return ProductMapper.INSTANCE.mapCoffin((Coffin) entity);
			} else if (type == ProductType.TOMBSTONE) {
				return ProductMapper.INSTANCE.mapTombstone((Tombstone) entity);
			} else if (type == ProductType.TRANSPORT) {
				return ProductMapper.INSTANCE.mapTransport((TransportPrice) entity);
			} else if (type == ProductType.MORTUARY) {
				return ProductMapper.INSTANCE.mapEmbalmingPrice((EmbalmingPrice) entity);
			} 
		} catch (Exception ex) {
			log.error(ex);
		}
		return ProductMapper.INSTANCE.map(entity);
	}
}
