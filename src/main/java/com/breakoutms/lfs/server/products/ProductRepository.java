package com.breakoutms.lfs.server.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.server.products.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

	Page<Product> findByProductType(Pageable pageable, ProductType productType);
	
}
