package com.breakoutms.lfs.server.products;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public Page<Product> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Product save(final Product entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public Product update(Integer id, Product entity) {
		if(entity == null) {
			throw ExceptionSupplier.nullUpdate("Product").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Product", id).get();
		}
		entity.setId(id);
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
