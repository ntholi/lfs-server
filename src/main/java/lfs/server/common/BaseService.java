package lfs.server.common;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author Ntholi Nkhatho
 *
 * @param <T> the entity
 * @param <I> the entity's id
 */
public interface BaseService<T, I> {

	public Optional<T> get(I id);
	public Page<T> all(Pageable pageable);
	public T save(final T entity);
	public T update(I id, T entity);
	public void delete(I id);
}
