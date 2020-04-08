package lfs.server.mortuary;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lfs.server.core.AuditableRepository;

@Repository
public interface CorpseRepository extends AuditableRepository<Corpse, String>{

//	@Query(value="SELECT EXISTS( SELECT 1 FROM corpse WHERE tag_no = :id LIMIT 1)", nativeQuery = true)
//	int exists(@Param("id") String tagNo);

	@Query("FROM NextOfKin where corpse.tagNo = :tagNo")
	List<NextOfKin> findNextOfKins(String tagNo);
	
}
