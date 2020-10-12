package com.breakoutms.lfs.server.mortuary.corpse;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.mortuary.corpse.model.NextOfKin;
import com.breakoutms.lfs.server.mortuary.corpse.model.OtherMortuary;
import com.breakoutms.lfs.server.sales.model.Quotation;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CorpseService {

	private final CorpseRepository repo;
	private final OtherMortuaryRepository otherMortuaryRepo;
	
	public Optional<Corpse> get(String id) {
		return repo.findById(id);
	}
	
	public Page<Corpse> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Corpse save(final Corpse entity) {
		addAssociations(entity);
		return repo.save(entity);
	}

	@Transactional
	public Corpse update(String id, Corpse updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Corpse").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Corpse", id));
		
		
		addAssociations(updatedEntity);
		
		if(entity.getTransferredFrom() != null) {
			OtherMortuary om = entity.getTransferredFrom();
			if(om.getId() == null && StringUtils.isNotBlank(om.getName())) {
				Optional<OtherMortuary> obj = otherMortuaryRepo.findFirstByName(om.getName());
				if(obj.isPresent()) {
					entity.setTransferredFrom(obj.get());
				}
			}
			else if(!otherMortuaryRepo.existsById(om.getId())) {
				om.setId(null);
			}
		}
		CorpseMapper.INSTANCE.update(updatedEntity, entity);
		entity.setNextOfKins(updatedEntity.getNextOfKins());
		return repo.save(entity);
	}

	private void addAssociations(Corpse corpse) {
		corpse.getNextOfKins().forEach(it ->
			it.setCorpse(corpse)
		);
		Quotation quotation = corpse.getQuotation();
		if(quotation == null) {
			quotation = new Quotation();
			corpse.setQuotation(quotation);
			quotation.setCorpse(corpse);
		}
	}
    
	public Page<Corpse> search(Specification<Corpse> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
    
	public void delete(String id) {
		repo.deleteById(id);
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

	public List<CorpseLookupProjection> lookup(String names) {
		return repo.lookup(names);
	}
}
