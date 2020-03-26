package lfs.server.mortuary;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CorpseService {

	private CorpseRepository repository;

	public CorpseService(CorpseRepository repository) {
		this.repository = repository;
	}
	
	public List<String> getReceivedBy(){
		return repository.getReceivedBy();
	}
}
