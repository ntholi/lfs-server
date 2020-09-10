package com.breakoutms.lfs.server.preneed.pricing;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

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
	
	@Transactional
	public FuneralScheme save(final FuneralScheme entity) {
		setAssociations(entity);
		return repo.save(entity);
	}
	
	@Transactional
	public FuneralScheme update(Integer id, FuneralScheme updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Funeral Scheme").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Funeral Scheme", id));

		setAssociations(updatedEntity);
		PreneedMapper.INSTANCE.update(updatedEntity, entity);
		
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
	
	protected void setAssociations(final FuneralScheme entity) {
		if(entity.getPremiums() != null) {
			entity.getPremiums().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
		if(entity.getDependentBenefits() != null) {
			entity.getDependentBenefits().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
		if(entity.getBenefits() != null) {
			entity.getBenefits().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
		if(entity.getPenaltyDeductibles() != null) {
			entity.getPenaltyDeductibles().forEach(it ->
				it.setFuneralScheme(entity)
			);
		}
	}

	public List<PenaltyDeductible> getPenaltyDeductibles(Integer funeralSchemeId) {
		return repo.getPenaltyDeductibles(funeralSchemeId);
	}

	public List<FuneralSchemeBenefit> getFuneralSchemeBenefits(Integer funeralSchemeId) {
		return repo.getFuneralSchemeBenefits(funeralSchemeId);
	}

	public List<DependentBenefit> getDependentBenefits(Integer funeralSchemeId) {
		return repo.getDependentBenefits(funeralSchemeId);
	}

	public List<Premium> getPremiums(Integer funeralSchemeId) {
		return repo.getPremiums(funeralSchemeId);
	}

	public Page<FuneralScheme> search(Specification<FuneralScheme> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
}
