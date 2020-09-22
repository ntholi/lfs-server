package com.breakoutms.lfs.server.mortuary.released;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.common.enums.ReleasedCorpseStatus;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReleasedCorpseService {

	private final ReleasedCorpseRepository repo;
	
	public Optional<ReleasedCorpse> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<ReleasedCorpse> all(PageRequest pageable) {
		Sort sort = Sort.by(Direction.ASC, "status").and(pageable.getSort());
		var pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return repo.findAll(pageRequest);
	}
	
	public Page<ReleasedCorpse> search(Specification<ReleasedCorpse> specs, Pageable pageable) {
		Sort sort = Sort.by(Direction.ASC, "status").and(pageable.getSort());
		var pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repo.findAll(Specification.where(specs), pageRequest);
    }
	
	@Transactional
	public ReleasedCorpse save(final ReleasedCorpse entity) {
		Corpse corpse = entity.getCorpse();
		corpse.setReleasedCorpse(entity);
		if(entity.getId() != null) {
			entity.setStatus(ReleasedCorpseStatus.RELEASED);
			return update(entity.getId(), entity);
		}
		else return repo.save(entity);
	}
	
	@Transactional
	public ReleasedCorpse update(Integer id, ReleasedCorpse updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Released Corpse").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Released Corpse", id));
		
		ReleasedCorpseMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
	
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
