package com.breakoutms.lfs.server.client_startup;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/get-startup-data")
@AllArgsConstructor
public class ClientStartupController {

	private StartupDataService service;
	
	@GetMapping
	public ResponseEntity<StartupData> get() {
		return ResponseEntity.of(service.get());
	}
}
