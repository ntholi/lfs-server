package com.breakoutms.lfs.server.mortuary;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.model.Corpse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CorpseService {

	private final CorpseRepository repo;
	
	public Optional<Corpse> get(String id) {
		return repo.findById(id);
	}
	
	public Page<Corpse> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Corpse save(final Corpse entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public Corpse update(String id, Corpse updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Corpse").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Corpse", id));
		
		CorpseMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(String id) {
		repo.deleteById(id);
	}
}
