package com.breakoutms.lfs.server.preneed.policy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.common.enums.PolicyStatus;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyLookupProjection;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyService {
	
	private final PolicyRepository repo;
	private final FuneralSchemeRepository funeralSchemeRepo;
	
	public Optional<Policy> get(String id) {
		return repo.findById(id);
	}
	
	public Page<Policy> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Policy save(Policy policy, final String funeralSchemeName) {
		FuneralScheme funeralScheme = getFuneralScheme(funeralSchemeName);
		Premium premium = funeralSchemeRepo.findPremium(funeralScheme, policy.getAge())
				.orElseThrow(() -> new InvalidOperationException(
						"Unable to determine Premium for '"+funeralSchemeName+"' funeral scheme "+
						"with policy holder's age at "+ policy.getAge()));
		policy.setFuneralScheme(funeralScheme);
		policy.setCoverAmount(premium.getCoverAmount());
		policy.setPremiumAmount(premium.getPremiumAmount());
		
		LocalDate activeDate = policy.getRegistrationDate()
				.plusMonths(funeralScheme.getMonthsBeforeActive());
		LocalDate today = LocalDate.now();
		
		if(today.isEqual(activeDate) || today.isAfter(activeDate)) {
			policy.setStatus(PolicyStatus.ACTIVE);
		}
		else {
			policy.setStatus(PolicyStatus.WAITING_PERIOD);
		}
		return repo.save(policy);
	}
	
	@Transactional
	public Policy update(String id, Policy updatedEntity, String funeralSchemeName) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Policy").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.policyNotFound(id));
		
		entity.setFuneralScheme(getFuneralScheme(funeralSchemeName));
		
		PreneedMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	protected FuneralScheme getFuneralScheme(final String funeralSchemeName) {
		return funeralSchemeRepo.findByName(funeralSchemeName)
				.orElseThrow(() -> new ObjectNotFoundException(
						"Funeral Scheme with name '"+ funeralSchemeName +"' not found"));
	}
	
	public Page<Policy> search(Specification<Policy> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	public void delete(String id) {
		repo.deleteById(id);
	}

	public List<PolicyLookupProjection> lookup(String names) {
		return repo.lookup(names);
	}
}
