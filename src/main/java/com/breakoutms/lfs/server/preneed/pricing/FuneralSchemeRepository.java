package com.breakoutms.lfs.server.preneed.pricing;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

@Repository
public interface FuneralSchemeRepository extends JpaRepository<FuneralScheme, Integer> {

	Optional<FuneralScheme> findByName(String name);
	
	@Query("FROM PenaltyDeductible e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<PenaltyDeductible> getPenaltyDeductibles(Integer id);

	@Query("FROM FuneralSchemeBenefit e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<FuneralSchemeBenefit> getFuneralSchemeBenefits(Integer id);

	@Query("FROM DependentBenefit e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<DependentBenefit> getDependentBenefits(Integer id);

	@Query("FROM Premium e WHERE e.funeralScheme.id = :id AND e.deleted=false")
	List<Premium> getPremiums(Integer id);
	
	@Query("FROM Premium e WHERE e.funeralScheme = :funeralScheme "
			+ "AND (:age >= e.minimumAge AND :age <= e.maximumAge) "
			+ "AND e.deleted=false")
	Optional<Premium> findPremium(FuneralScheme funeralScheme, int age);
}
