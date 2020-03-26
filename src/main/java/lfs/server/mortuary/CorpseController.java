package lfs.server.mortuary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/received-by")
	public List<String> getRreceivedBy() {
		return service.getReceivedBy();
	}
}
