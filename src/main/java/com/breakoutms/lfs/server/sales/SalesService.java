package com.breakoutms.lfs.server.sales;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.sales.model.Sales;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalesService {

	private final SalesRepository repo;
	
	public Optional<Sales> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<Sales> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Sales save(final Sales entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public Sales update(Integer id, Sales entity) {
		if(entity == null) {
			throw ExceptionSupplier.nullUpdate("Sales").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Sales", id).get();
		}
		entity.setId(id);
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
