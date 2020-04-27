package com.breakoutms.lfs.server.preneed;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.model.Policy;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyService {
	
	private final PolicyRepository repo;
	
	public Optional<Policy> get(String id) {
		return repo.findById(id);
	}
	
	public Page<Policy> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public Policy save(final Policy entity) {
		return repo.save(entity);
	}
	
	public Policy update(String id, Policy entity) {
		if(entity == null) {
			throw ExceptionSupplier.notFoundOnUpdate("Policy").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Policy", id).get();
		}
		return repo.save(entity);
	}

	public void delete(String id) {
		repo.deleteById(id);
	}
}
