package lfs.server.mortuary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public Corpse save(@RequestBody Corpse corpse) {
		return service.save(corpse);
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
