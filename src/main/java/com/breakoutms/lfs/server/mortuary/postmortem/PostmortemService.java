package com.breakoutms.lfs.server.mortuary.postmortem;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.mortuary.postmortem.model.Postmortem;
import com.breakoutms.lfs.server.mortuary.request.MortuaryRequestRepository;
import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequest;
import com.breakoutms.lfs.server.transport.Transport;
import com.breakoutms.lfs.server.transport.TransportRepository;
import com.breakoutms.lfs.server.transport.Vehicle;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostmortemService {

	private final PostmortemRepository repo;
	private final TransportRepository transportRepo;
	private final MortuaryRequestRepository requestRepo;

	public Optional<Postmortem> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<Postmortem> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Postmortem save(final Postmortem entity) {
		Integer requestId = entity.getPostmortemRequest().getId();
		MortuaryRequest undertakerRequest = requestRepo.findById(requestId)
				.orElseThrow(ExceptionSupplier.notFound("Transfer Out Request", requestId));
		if (undertakerRequest instanceof PostmortemRequest) {
			var transferRequest = (PostmortemRequest) undertakerRequest;
			transferRequest.setSeen(true);
			transferRequest.setProcessed(true);
		}
		else {
			throw new InvalidOperationException("Undertaker Request of id: '"+
					requestId+"' is not a Postmortem Request");
		}
		setAssociations(entity);
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
		setAssociations(updatedEntity);
		return repo.save(entity);
	}
	
	private void setAssociations(Postmortem entity) {
		Transport transport = entity.getTransport();
		if(transport != null) {
			Vehicle v = transport.getVehicle();
			if(v != null) {
				transportRepo.findVehicleByRegNumber(v.getRegistrationNumber())
					.ifPresent(it -> entity.getTransport().setVehicle(it));
			}
		}
	}
	
	public Page<Postmortem> search(Specification<Postmortem> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
}
