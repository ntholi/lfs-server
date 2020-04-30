package com.breakoutms.lfs.server.preneed.payment;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyPaymentService {

	private final PolicyPaymentRepository repo;
	
	public Optional<PolicyPayment> get(Long id) {
		return repo.findById(id);
	}
	
	public Page<PolicyPayment> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public PolicyPayment save(final PolicyPayment entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public PolicyPayment update(Long id, PolicyPayment entity) {
		if(entity == null) {
			throw ExceptionSupplier.notFoundOnUpdate("Funeral Scheme").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Funeral Scheme", id).get();
		}
		entity.setId(id);
		return repo.save(entity);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}
}
