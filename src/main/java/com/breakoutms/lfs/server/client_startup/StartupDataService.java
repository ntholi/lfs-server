package com.breakoutms.lfs.server.client_startup;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StartupDataService {

	private final FuneralSchemeRepository funeralSchemeRepo;
	private final BranchRepository branchRepo;

	public Optional<StartupData> get() {
		StartupData data = new StartupData();
		data.setBranches(branchRepo.findAllBranchNames());
		data.setFuneralSchemes(funeralSchemeRepo.findAllNames());
		
		return Optional.of(data);
	}
}
