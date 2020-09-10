package com.breakoutms.lfs.server.products;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.products.model.Product;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

	private final ProductRepository repo;
	
	public Optional<Product> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<Product> search(Specification<Product> specs, Pageable pageable, String productType) {
		if(StringUtils.isBlank(productType) 
				|| productType.strip().equalsIgnoreCase("ALL")) {
			return repo.findAll(specs, pageable);
		}
		String[] arr = productType.split(",");
		ProductType[] types = new ProductType[arr.length];
		for (int i = 0; i < arr.length; i++) {
			types[i] = ProductType.fromString(arr[i]);
		}
		return repo.findByProductTypeIn(types, Specification.where(specs), pageable);
	}
	
	public Page<Product> all(Pageable pageable) {
		return repo.findAll(pageable);
	}

	public Page<Product> search(Specification<Product> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	@Transactional
	public Product save(final Product entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public Product update(Integer id, Product updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Product").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Product", id));
		
		ProductMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
