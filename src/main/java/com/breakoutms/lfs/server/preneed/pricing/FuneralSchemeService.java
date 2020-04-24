package com.breakoutms.lfs.server.preneed.pricing;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuneralSchemeService {

	private final FuneralSchemeRepository repo;
	
	public Optional<FuneralScheme> get(Integer id) {
		return repo.findById(id);
	}
	
	public Page<FuneralScheme> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public FuneralScheme save(final FuneralScheme entity) {
		return repo.save(entity);
	}
	
	public FuneralScheme update(Integer id, FuneralScheme entity) {
		if(entity == null) {
			throw ExceptionSupplier.notFoundOnUpdate("Funeral Scheme").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Funeral Scheme", id).get();
		}
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public List<PenaltyDeductible> getPenaltyDeductibles(Integer id) {
		return repo.getPenaltyDeductibles(id);
	}

	public List<FuneralSchemeBenefit> getFuneralSchemeBenefit(Integer id) {
		return repo.getFuneralSchemeBenefit(id);
	}

	public List<DependentBenefit> getDependentBenefits(Integer id) {
		return repo.getDependentBenefits(id);
	}

	public List<Premium> getPremiums(Integer id) {
		return repo.getPremiums(id);
	}
}
