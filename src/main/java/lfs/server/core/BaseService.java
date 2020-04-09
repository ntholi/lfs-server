package lfs.server.core;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lfs.server.exceptions.ExceptionSupplier;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Ntholi Nkhatho
 *
 * @param <T> the entity
 * @param <I> the entity's id
 */
@Log4j2
public abstract class BaseService<T, I, R extends AuditableRepository<T, I>> {

	protected R repo;
	private final String className;
	
	@Autowired
	@SuppressWarnings("unchecked")
	public BaseService(R repo) {
		this.repo = repo;
		Class<T> type = null;
		try{
			type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}catch(Exception ex) {
			log.error("Error determining service's entity type", ex);
		}
		className = type != null? type.getSimpleName(): "";
	}

	public Optional<T> get(I id) {
		return repo.findById(id);
	}
	
	public Page<T> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public T save(final T entity) {
		return repo.save(entity);
	}
	
	public T update(I id, T entity) {
		if(entity == null) {
			throw new NullPointerException(className+" object provide is null, cannot update a null object");
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound(className, id).get();
		}
		return repo.save(entity);
	}

	public void delete(I id) {
		repo.deleteById(id);
	}
}
