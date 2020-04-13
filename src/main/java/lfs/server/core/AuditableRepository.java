package lfs.server.core;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AuditableRepository<T, ID> extends JpaRepository<T, ID> {

	@Override
	@Query("SELECT e from #{#entityName} e where e.id = ?1 and e.deleted=false")
	Optional<T> findById(ID id);

	@Override
	@Query("SELECT e FROM #{#entityName} e WHERE e.deleted=false")
	abstract Page<T> findAll(Pageable pageable) ;

	@Override
	@Query("SELECT e FROM #{#entityName} e WHERE e.deleted=false")
	abstract List<T> findAll() ;

	@Override
	@Query("UPDATE #{#entityName} e SET e.deleted=true WHERE e.id=?1")
	@Modifying
	abstract void deleteById(ID id);

	@Override
	default void delete(T entity) {
		throw new NotImplementedException("use deleteById(id)");
	}

}
