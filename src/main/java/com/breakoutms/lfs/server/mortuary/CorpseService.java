package com.breakoutms.lfs.server.mortuary;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CorpseService {

	private final OtherMortuaryRepository otherMortuaryRepo;
	private final CorpseRepository repo;
	
	public Optional<Corpse> get(String id) {
		return repo.findById(id);
	}
	
	public Page<Corpse> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Corpse save(final Corpse corpse) {
		OtherMortuary om = corpse.getTransferredFrom();
		if(om != null
				&& StringUtils.isNotBlank(om.getName())) {
			Optional<OtherMortuary> obj = otherMortuaryRepo.findFirstByName(om.getName());
			if(obj.isPresent()) {
				corpse.setTransferredFrom(obj.get());
			}
		}
		return repo.save(corpse);
	}

	@Transactional
	public Corpse update(String id, Corpse corpse) {
		if(corpse == null) {
			throw ExceptionSupplier.nullUpdate("Corpse").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Corpse", id).get();
		}
		if(corpse.getTransferredFrom() != null) {
			OtherMortuary om = corpse.getTransferredFrom();
			if(om.getId() == null && StringUtils.isNotBlank(om.getName())) {
				Optional<OtherMortuary> obj = otherMortuaryRepo.findFirstByName(om.getName());
				if(obj.isPresent()) {
					corpse.setTransferredFrom(obj.get());
				}
			}
			else if(!otherMortuaryRepo.existsById(om.getId())) {
				throw new ObjectNotFoundException("Unable to update corpse with id "+
						id+" OtherMortuary object with id '"+om.getId()+"' not found");
			}
		}
		return repo.save(corpse);
	}
	
	public void delete(String id) {
		repo.deleteById(id);
	}
	
	public List<NextOfKin> getNextOfKins(String id) {
		return repo.findNextOfKins(id);
	}
	
	public List<OtherMortuary> getOtherMortuaries(){
		return otherMortuaryRepo.findAll();
	}

	public Optional<OtherMortuary> getTransforedFrom(int id) {
		return otherMortuaryRepo.findById(id);
	}
}
