package lfs.server.mortuary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lfs.server.exceptions.ExceptionSupplier;

@Service
public class CorpseService {

	private CorpseRepository repository;
	private OtherMortuaryRepository otherMortuaryRepository;

	@Autowired
	public CorpseService(CorpseRepository repository, OtherMortuaryRepository otherMortuaryRepository) {
		this.repository = repository;
		this.otherMortuaryRepository = otherMortuaryRepository;
	}
	
	public Corpse save(Corpse corpse) {
		return repository.save(corpse);
	}
	
	public Corpse update(String tagNo, Corpse corpse) {
		if(!repository.existsById(tagNo)) {
			throw ExceptionSupplier.corpseNotFound(tagNo).get();
		}
		return repository.save(corpse);
	}
	
	public Iterable<OtherMortuary> getOtherMortuaries(){
		return otherMortuaryRepository.findAll();
	}

	public Iterable<OtherMortuary> getOtherMortuaries(String tagNo) {
		Corpse corpse = repository.findById(tagNo)
				.orElseThrow(ExceptionSupplier.corpseNotFound(tagNo));
		return otherMortuaryRepository.findByCorpse(corpse);
	}
}
