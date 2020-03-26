package lfs.server.mortuary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/corpses")
public class CorpseController {

	
	@GetMapping("/received-by")
	public String getRreceivedBy() {
		return "Hello World";
	}
}
