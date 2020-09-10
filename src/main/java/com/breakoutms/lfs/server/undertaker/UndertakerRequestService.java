package com.breakoutms.lfs.server.undertaker;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UndertakerRequestService {

	private UndertakerRequestRepository repo;

	public Page<UndertakerRequest> all(Pageable p) {
		Sort sort = p.getSort().and(Sort.by("processed").ascending());
		PageRequest pageRequest = PageRequest.of(p.getPageNumber(), 
				p.getPageSize(), sort);
		
		return repo.findByProcessed(pageRequest, false);
	}

	public Page<UndertakerRequest> search(Specification<UndertakerRequest> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	public List<UndertakerRequest> lookup(String names) {
		return repo.lookup(names);
	}
}
