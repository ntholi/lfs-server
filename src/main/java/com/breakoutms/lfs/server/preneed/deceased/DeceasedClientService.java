package com.breakoutms.lfs.server.preneed.deceased;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseRepository;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeceasedClientService {


	private final DeceasedClientRepository repo;
	private final PolicyRepository policyRepo;
	private final CorpseRepository corpseRepo;
	
	public Optional<DeceasedClient> get(Long id) {
		return repo.findById(id);
	}
	
	public Page<DeceasedClient> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public DeceasedClient save(final DeceasedClient entity, String policyNumber) {
		return repo.save(entity);
	}
	
	@Transactional(readOnly = true)
	public List<CorpseLookupProjection> lookup(String policyNumber) {
		Policy policy = policyRepo.findById(policyNumber)
				.orElseThrow(ExceptionSupplier.policyNotFound(policyNumber));
		String names = policy.getFullName();
		return corpseRepo.lookup(names);
	}
	
	@Transactional
	public DeceasedClient update(Long id, DeceasedClient updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Deceased Client").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Deceased Client", id));
		
		DeceasedClientMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}
}
