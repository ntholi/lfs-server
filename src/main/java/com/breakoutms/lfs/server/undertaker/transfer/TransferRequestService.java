package com.breakoutms.lfs.server.undertaker.transfer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestMapper;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestRepository;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestService;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;

@Service
public class TransferRequestService extends UndertakerRequestService<TransferRequest> {

	public TransferRequestService(UndertakerRequestRepository<TransferRequest> repo) {
		super(repo);
	}

	@Transactional
	public TransferRequest update(Long id, TransferRequest updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Transfer Request").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Transfer Request", id));
		
		UndertakerRequestMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
}
