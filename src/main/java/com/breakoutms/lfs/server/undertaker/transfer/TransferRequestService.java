package com.breakoutms.lfs.server.undertaker.transfer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestMapper;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransferRequestService {

	private final TransferRequestRepository repo;

	public Optional<TransferRequest> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<TransferRequest> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public TransferRequest save(final TransferRequest entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public TransferRequest update(Integer id, TransferRequest updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Transfer Request").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Transfer Request", id));
		
		UndertakerRequestMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
	
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
