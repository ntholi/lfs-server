package lfs.server.preneed.pricing;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lfs.server.common.BaseService;
import lfs.server.exceptions.ExceptionSupplier;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FuneralSchemeService implements BaseService<FuneralScheme, Integer> {
	
	private FuneralSchemeRepository repo;
	
	public Optional<FuneralScheme> get(Integer id) {
		return repo.findById(id);
	}

	@Override
	public Page<FuneralScheme> all(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public FuneralScheme save(FuneralScheme entity) {
		return repo.save(entity);
	}

	@Override
	public FuneralScheme update(Integer id, FuneralScheme entity) {
		if(entity == null) {
			throw new NullPointerException("Corpse object provided is null");
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("FuneralScheme", id).get();
		}
		return repo.save(entity);
	}

	@Override
	public void delete(Integer id) {
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("FuneralScheme", id));
		repo.delete(entity);
	}

}
