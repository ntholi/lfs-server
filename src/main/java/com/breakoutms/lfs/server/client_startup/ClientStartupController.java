package com.breakoutms.lfs.server.client_startup;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.branch.BranchService;
import com.breakoutms.lfs.server.branch.model.Branch;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/startup-data")
@AllArgsConstructor
public class ClientStartupController {

	private StartupDataService service;
	private BranchService branchService;
	
	@GetMapping
	public ResponseEntity<StartupData> get() {
		Optional<StartupData> value = service.get();
		if(value.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		else return ResponseEntity.ok(value.get());
	}
	
	@GetMapping("/branches")
	public List<Branch> branches() {
		return branchService.all();
	}
}
