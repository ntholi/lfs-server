package com.breakoutms.lfs.server.branch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/branches")
@AllArgsConstructor
public class BranchController {

	private BranchRepository repo;

	@GetMapping("/{id}")
	public ResponseEntity<Branch> get(@PathVariable Integer id) {
		return repo.findById(id)
				.map(ResponseEntity::ok)
				.orElseThrow(ExceptionSupplier.notFound("Branch", id));
	}
	
	@GetMapping("/search")
	public ResponseEntity<Branch> findByName(@RequestParam String name) {
		return repo.findByName(name)
				.map(ResponseEntity::ok)
				.orElseThrow(ExceptionSupplier.notFound("Branch with name '"+name+"' not found"));
	}
	
	@GetMapping
	public Iterable<Branch> all() {
		return repo.findAll();
	}
}
