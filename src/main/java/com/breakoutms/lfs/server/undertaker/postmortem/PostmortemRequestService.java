package com.breakoutms.lfs.server.undertaker.postmortem;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.request.MortuaryRequestMapper;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostmortemRequestService {

	private final PostmortemRequestRepository repo;

	public Optional<PostmortemRequest> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<PostmortemRequest> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public Page<PostmortemRequest> search(Specification<PostmortemRequest> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	@Transactional
	public PostmortemRequest save(final PostmortemRequest entity) {
		return repo.save(entity);
	}

	@Transactional
	public PostmortemRequest update(Integer id, PostmortemRequest updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Postmortem Request").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Postmortem Request", id));
		
		MortuaryRequestMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
}
