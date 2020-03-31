package lfs.server.mortuary;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/corpses")
public class CorpseController {

	private CorpseService service;
	
	@Autowired
	public CorpseController(CorpseService service) {
		super();
		this.service = service;
	}

	@PostMapping
	public Corpse save(@Valid @RequestBody Corpse corpse) {
		return service.save(corpse);
	}
	
	@GetMapping
	public Page<Corpse> all(@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
		var page = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return service.all(page);
	}
	
	@GetMapping("/{tagNo}")
	public Optional<Corpse> get(@PathVariable String tagNo) {
		return service.get(tagNo);
	}
	
	
	@PutMapping("/{tagNo}")
	public Corpse update(@PathVariable String tagNo, @RequestBody Corpse corpse) {
		return service.update(tagNo, corpse);
	}
	
	
	@GetMapping("/{tagNo}/transferred-from")
	public OtherMortuary getTransforedFrom(@PathVariable String tagNo) {
		return service.getOtherMortuaries(tagNo).iterator().next();
	}
	
	@GetMapping("/other-mortuaries")
	public Iterable<OtherMortuary> getOtherMortuaries() {
		return service.getOtherMortuaries();
	}
}
