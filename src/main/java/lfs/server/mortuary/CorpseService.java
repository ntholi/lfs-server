package lfs.server.mortuary;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lfs.server.core.BaseService;
import lfs.server.exceptions.ExceptionSupplier;
import lfs.server.exceptions.ObjectNotFoundException;

@Service
public class CorpseService extends BaseService<Corpse, String, CorpseRepository> {


	private OtherMortuaryRepository otherMortuaryRepo;

	public CorpseService(OtherMortuaryRepository otherMortuaryRepo, CorpseRepository repo) {
		super(repo);
		this.otherMortuaryRepo = otherMortuaryRepo;
	}
	
	@Transactional
	@Override
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
	@Override
	public Corpse update(String tagNo, Corpse corpse) {
		if(corpse == null) {
			throw new NullPointerException("Corpse object provided is null");
		}
		if(!repo.existsById(tagNo)) {
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
			else if(!otherMortuaryRepo.existsById(om.getId())) {
				throw new ObjectNotFoundException("OtherMortuary object with id '"+
						om.getId()+"' not found");
			}
		}
		return repo.save(corpse);
	}
	
	public List<NextOfKin> getNextOfKins(String tagNo) {
		return repo.findNextOfKins(tagNo);
	}
	
	public List<OtherMortuary> getOtherMortuaries(){
		return otherMortuaryRepo.findAll();
	}

	public Optional<OtherMortuary> getTransforedFrom(int id) {
		return otherMortuaryRepo.findById(id);
	}
}
