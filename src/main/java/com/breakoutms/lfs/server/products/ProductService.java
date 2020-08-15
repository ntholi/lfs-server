package com.breakoutms.lfs.server.products;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Page<Product> all(String productType, Pageable pageable) {
		Page<Product> page;
		productType = productType.replace(",", "");
		if(productType.equalsIgnoreCase("All")) {
			page = repo.findAll(pageable);
		}
		else {
			page = repo.findByProductType(pageable, ProductType.fromString(productType));
		}
		return page;
	}
	
	public Page<Product> all(Pageable pageable) {
		return repo.findAll(pageable);
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
