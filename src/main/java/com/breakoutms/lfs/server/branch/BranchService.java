package com.breakoutms.lfs.server.branch;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BranchService {
	
	private final BranchRepository repo;
	
	//TODO: MAKE SURE THAT CACHING WORKS AS INTENDED  - PLUS - add to update method once created
	@Cacheable("branches")
	public Optional<Branch> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<Branch> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public List<Branch> all() {
		return repo.findAll();
	}
	
	public Page<Branch> search(Specification<Branch> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	@Transactional
	public Branch save(final Branch entity) {
		if(entity.getSyncNumber() == null || entity.getSyncNumber() == 0) {
			Integer syncNumber = repo.getLastSyncNumber()+1;
			entity.setSyncNumber(syncNumber);
		}
		return repo.save(entity);
	}
	
	@Transactional
	public Branch update(Integer id, Branch updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Branch").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Branch", id));
		
		BranchMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
	
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
