package com.breakoutms.lfs.server.mortuary.postmortem;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.postmortem.model.Postmortem;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostmortemService {

	private final PostmortemRepository repo;

	public Optional<Postmortem> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<Postmortem> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Postmortem save(final Postmortem entity) {
		return repo.save(entity);
	}

	@Transactional
	public Postmortem update(Integer id, Postmortem updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Postmortem Request").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Postmortem Request", id));
		
		PostmortemMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
}
