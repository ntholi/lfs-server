package com.breakoutms.lfs.server.reception.embalming;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.reception.embalming.model.EmbalmingRequest;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmbalmingRequestService {

	private final EmbalmingRequestRepository repo;

	public Optional<EmbalmingRequest> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<EmbalmingRequest> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public EmbalmingRequest save(final EmbalmingRequest entity) {
		return repo.save(entity);
	}

	@Transactional
	public EmbalmingRequest update(Integer id, EmbalmingRequest updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Embalming Request").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Embalming Request", id));
		
		UndertakerRequestMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	
	public Page<EmbalmingRequest> search(Specification<EmbalmingRequest> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
