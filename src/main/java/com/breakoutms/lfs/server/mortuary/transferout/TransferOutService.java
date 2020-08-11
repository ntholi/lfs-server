package com.breakoutms.lfs.server.mortuary.transferout;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.mortuary.transferout.model.TransferOut;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestRepository;
import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransferOutService {

	private final TransferOutRepository repo;
	private final UndertakerRequestRepository requestRepo;
	
	public Optional<TransferOut> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<TransferOut> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public TransferOut save(final TransferOut entity) {
		Integer requestId = entity.getTransferRequest().getId();
		UndertakerRequest undertakerRequest = requestRepo.findById(requestId)
				.orElseThrow(ExceptionSupplier.notFound("Transfer Out Request", requestId));
		if (undertakerRequest instanceof TransferRequest) {
			var transferRequest = (TransferRequest) undertakerRequest;
			transferRequest.setSeen(true);
			transferRequest.setProcessed(true);
		}
		else {
			throw new InvalidOperationException("Undertaker Request of id: '"+
					requestId+"' is not a Transfer Request");
		}
		return repo.save(entity);
	}
	
	@Transactional
	public TransferOut update(Integer id, TransferOut updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Transfer Out").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Transfer Out", id));
		
		TransferOutMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
