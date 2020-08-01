package com.breakoutms.lfs.server.undertaker;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.undertaker.model.PostmortemRequest;

@Service
public class PostmortemRequestService extends UndertakerRequestService<PostmortemRequest> {

	public PostmortemRequestService(UndertakerRequestRepository<PostmortemRequest> repo) {
		super(repo);
	}

	@Transactional
	public PostmortemRequest update(Long id, PostmortemRequest updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Postmortem Request").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Postmortem Request", id));
		
		UndertakerRequestMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
}
