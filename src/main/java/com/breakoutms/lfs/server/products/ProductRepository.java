package com.breakoutms.lfs.server.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.server.products.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product>{

	Page<Product> findByProductTypeIn(Specification<Product> specs, Pageable pageable, ProductType[] productType);
	
}
