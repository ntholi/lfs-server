package com.breakoutms.lfs.server.mortuary.embalming;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.embalming.model.Embalming;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmbalmingService {

	private final EmbalmingRepository repo;
	
	public Optional<Embalming> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<Embalming> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Embalming save(final Embalming entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public Embalming update(Integer id, Embalming updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Embalming").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Embalming", id));
		
		EmbalmingMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
