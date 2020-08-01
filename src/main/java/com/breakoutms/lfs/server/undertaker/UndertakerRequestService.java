package com.breakoutms.lfs.server.undertaker;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class UndertakerRequestService<T extends UndertakerRequest> {

	protected final UndertakerRequestRepository<T> repo;
	
	public Optional<T> get(Long id) {
		return repo.findById(id);
	}
	
	public Page<T> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public T save(final T entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public abstract T update(Long id, T updatedEntity);

	public void delete(Long id) {
		repo.deleteById(id);
	}
}
