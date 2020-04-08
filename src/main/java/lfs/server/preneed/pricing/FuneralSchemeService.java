package lfs.server.preneed.pricing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lfs.server.core.BaseService;

@Service
public class FuneralSchemeService extends BaseService<FuneralScheme, Integer, FuneralSchemeRepository>{

	@Autowired
	public FuneralSchemeService(FuneralSchemeRepository repo) {
		super(repo);
	}

}
