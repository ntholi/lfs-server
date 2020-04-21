package com.breakoutms.lfs.server.mortuary;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.core.AuditableRepository;

@Repository
public interface CorpseRepository extends AuditableRepository<Corpse, String>{

//	@Query(value="SELECT EXISTS( SELECT 1 FROM corpse WHERE tag_no = :id LIMIT 1)", nativeQuery = true)
//	int exists(@Param("id") String tagNo);

	//TODO: don't include deleted NextOfKin
	@Query("FROM NextOfKin where corpse.tagNo = :tagNo")
	List<NextOfKin> findNextOfKins(String tagNo);
	
}
