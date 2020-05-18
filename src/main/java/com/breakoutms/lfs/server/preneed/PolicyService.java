package com.breakoutms.lfs.server.preneed;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyService {
	
	private final PolicyRepository repo;
	private final FuneralSchemeRepository funeralSchemeRepo;
	public static final String NO_POLICY_ERROR = "Policy Number '%s' not found";
	
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
						"Unable to determine Premium for "+funeralSchemeName+" funeral scheme "+
						"with policy holder's age at "+ policy.getAge()));
		policy.setFuneralScheme(funeralScheme);
		policy.setCoverAmount(premium.getCoverAmount());
		policy.setPremiumAmount(premium.getPremiumAmount());
		return repo.save(policy);
	}
	
	@Transactional
	public Policy update(String id, Policy entity, String funeralSchemeName) {
		if(entity == null) {
			throw ExceptionSupplier.nullUpdate("Policy").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound(String.format(NO_POLICY_ERROR, id)).get();
		}
		entity.setFuneralScheme(getFuneralScheme(funeralSchemeName));
		return repo.save(entity);
	}

	protected FuneralScheme getFuneralScheme(final String funeralSchemeName) {
		return funeralSchemeRepo.findByName(funeralSchemeName)
				.orElseThrow(() -> new ObjectNotFoundException(
						"Funeral Scheme with name '"+ funeralSchemeName +"' not found"));
	}
	
	public void delete(String id) {
		repo.deleteById(id);
	}
}
