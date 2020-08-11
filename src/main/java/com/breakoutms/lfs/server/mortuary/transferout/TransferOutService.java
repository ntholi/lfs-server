package com.breakoutms.lfs.server.mortuary.transferout;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.transferout.model.TransferOut;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransferOutService {

	private final TransferOutRepository repo;
	
	public Optional<TransferOut> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<TransferOut> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public TransferOut save(final TransferOut entity) {
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
