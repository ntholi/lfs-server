package com.breakoutms.lfs.server.products;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.common.enums.EmbalmingType;
import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.common.enums.TransportType;
import com.breakoutms.lfs.server.products.model.EmbalmingPrice;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.TransportPrice;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product>{

	Page<Product> findByProductTypeIn(ProductType[] productType, Specification<Product> specs, Pageable pageable);

	Page<Product> findByProductTypeIn(ProductType[] types, Pageable pageable);

	Optional<Product> findByName(String string);

	@Query("from EmbalmingPrice where embalmingType = :embalmingType")
	Optional<EmbalmingPrice> findByEmbalmingType(EmbalmingType embalmingType);

	@Query("from TransportPrice where transportType = :transportType AND to = :location")
	Optional<TransportPrice> findByTransportByType(TransportType transportType, String location);
}
