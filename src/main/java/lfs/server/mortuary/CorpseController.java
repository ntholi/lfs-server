package lfs.server.mortuary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/other-mortuaries")
	public Iterable<OtherMortuary> getOtherMortuaries() {
		return service.getOtherMortuaries();
	}
	
	@GetMapping("/{tagNo}/transferred-from")
	public OtherMortuary getTransforedFrom(@PathVariable String tagNo) {
		return service.getOtherMortuaries(tagNo).iterator().next();
	}
}
