package com.breakoutms.lfs.server.preneed.pricing;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.core.AuditableRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

@Repository
public interface FuneralSchemeRepository extends AuditableRepository<FuneralScheme, Integer> {

	@Query("FROM PenaltyDeductible e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<PenaltyDeductible> getPenaltyDeductibles(Integer id);

	@Query("FROM FuneralSchemeBenefit e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<FuneralSchemeBenefit> getFuneralSchemeBenefit(Integer id);

	@Query("FROM DependentBenefit e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<DependentBenefit> getDependentBenefits(Integer id);

	@Query("FROM Premium e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<Premium> getPremiums(Integer id);

}
