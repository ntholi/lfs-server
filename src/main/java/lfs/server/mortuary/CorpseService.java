package lfs.server.mortuary;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lfs.server.exceptions.ExceptionSupplier;
import lfs.server.exceptions.ObjectNotFoundException;

@Service
public class CorpseService {

	private CorpseRepository corpseRepo;
	private OtherMortuaryRepository otherMortuaryRepo;

	@Autowired
	public CorpseService(CorpseRepository repository, OtherMortuaryRepository otherMortuaryRepository) {
		this.corpseRepo = repository;
		this.otherMortuaryRepo = otherMortuaryRepository;
	}

	public Corpse get(String tagNo) {
		return corpseRepo.findById(tagNo).orElse(null);
	}

	public Page<Corpse> all(PageRequest pageable) {
		return corpseRepo.findAll(pageable);
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
		return corpseRepo.save(corpse);
	}

	@Transactional
	public Corpse update(String tagNo, Corpse corpse) {
		if(corpse == null) {
			throw new NullPointerException("Corpse object provided is null");
		}
		if(corpseRepo.exists(tagNo) == 0) {
			throw ExceptionSupplier.corpseNotFound(tagNo).get();
		}
		if(corpse.getTransferredFrom() != null) {
			OtherMortuary om = corpse.getTransferredFrom();
			if(om.getId() == null && StringUtils.isNotBlank(om.getName())) {
				Optional<OtherMortuary> obj = otherMortuaryRepo.findFirstByName(om.getName());
				if(obj.isPresent()) {
					corpse.setTransferredFrom(obj.get());
				}
			}
			else if(otherMortuaryRepo.exists(om.getId()) == 0) {
				throw new ObjectNotFoundException("OtherMortuary object with id '"+
						om.getId()+"' not found");
			}
		}
		return corpseRepo.save(corpse);
	}

	public void delete(String tagNo) {
		Corpse corpse = corpseRepo.findById(tagNo)
				.orElseThrow(ExceptionSupplier.corpseNotFound(tagNo));
		corpseRepo.delete(corpse);
	}
	public Iterable<OtherMortuary> getOtherMortuaries(){
		return otherMortuaryRepo.findAll();
	}

	public List<OtherMortuary> getOtherMortuaries(String tagNo) {
		Corpse corpse = corpseRepo.findById(tagNo)
				.orElseThrow(ExceptionSupplier.corpseNotFound(tagNo));
		return otherMortuaryRepo.findByCorpse(corpse);
	}
}
