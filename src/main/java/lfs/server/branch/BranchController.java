package lfs.server.branch;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/branches")
@AllArgsConstructor
public class BranchController {

	private BranchRepository repo;

	@GetMapping("/{id}")
	public ResponseEntity<Branch> get(@PathVariable Integer id) {
		var item = repo.findById(id);
		if(item.isPresent()) {
			return new ResponseEntity<>(item.get(), HttpStatus.OK);	
		}
		else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/search")
	public ResponseEntity<Branch> findByName(@RequestParam String name) {
		var item = repo.findByName(name);
		if(item.isPresent()) {
			return new ResponseEntity<>(item.get(), HttpStatus.OK);	
		}
		else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping
	public Iterable<Branch> all() {
		return repo.findAll();
	}
}
