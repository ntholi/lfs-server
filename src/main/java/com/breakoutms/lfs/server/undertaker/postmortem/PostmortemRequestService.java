package com.breakoutms.lfs.server.undertaker.postmortem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestMapper;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestRepository;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestService;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;

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
