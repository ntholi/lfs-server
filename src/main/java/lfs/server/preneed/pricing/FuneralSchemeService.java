package lfs.server.preneed.pricing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lfs.server.core.BaseService;

@Service
public class FuneralSchemeService extends BaseService<FuneralScheme, Integer, FuneralSchemeRepository>{

	@Autowired
	public FuneralSchemeService(FuneralSchemeRepository repo) {
		super(repo);
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
