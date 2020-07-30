package com.breakoutms.lfs.server.client_startup;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/get-startup-data")
@AllArgsConstructor
public class ClientStartupController {

	@GetMapping
	public ResponseEntity<StartupData> get() {
		StartupData data = new StartupData();
		data.setName("Hello World");
		return ResponseEntity.of(Optional.of(data));
	}
}
