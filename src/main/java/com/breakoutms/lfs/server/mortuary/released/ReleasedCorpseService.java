package com.breakoutms.lfs.server.mortuary.released;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReleasedCorpseService {

	private final ReleasedCorpseRepository repo;
	
	public Optional<ReleasedCorpse> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<ReleasedCorpse> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public ReleasedCorpse save(final ReleasedCorpse entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public ReleasedCorpse update(Integer id, ReleasedCorpse updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("ReleasedCorpse").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("ReleasedCorpse", id));
		
		ReleasedCorpseMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
