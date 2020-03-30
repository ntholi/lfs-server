package lfs.server.mortuary;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

	public Corpse save(Corpse corpse) {
		return corpseRepo.save(corpse);
	}
	
	public Optional<Corpse> get(String tagNo) {
		return corpseRepo.findById(tagNo);
	}

	public Page<Corpse> all(PageRequest pageable) {
		return corpseRepo.findAll(pageable);
	}

	public Corpse update(String tagNo, Corpse corpse) {
		if(!corpseRepo.existsById(tagNo)) {
			throw ExceptionSupplier.corpseNotFound(tagNo).get();
		}
		if(corpse.getTransferredFrom() != null 
				&& corpse.getTransferredFrom().getId() != null 
				&& !otherMortuaryRepo.existsById(corpse.getTransferredFrom().getId())) {
			throw new ObjectNotFoundException("OtherMortuary object with id "+
				corpse.getTransferredFrom().getId()+" not found");
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

	public Iterable<OtherMortuary> getOtherMortuaries(String tagNo) {
		Corpse corpse = corpseRepo.findById(tagNo)
				.orElseThrow(ExceptionSupplier.corpseNotFound(tagNo));
		return otherMortuaryRepo.findByCorpse(corpse);
	}
}
