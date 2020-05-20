package com.breakoutms.lfs.server.mortuary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CorpseRepository extends JpaRepository<Corpse, String>{

//	@Query(value="SELECT EXISTS( SELECT 1 FROM corpse WHERE tag_no = :id LIMIT 1)", nativeQuery = true)
//	int exists(@Param("id") String tagNo);

	//TODO: don't include deleted NextOfKin
	@Query("FROM NextOfKin where corpse.tagNo = :tagNo AND deleted = false")
	List<NextOfKin> findNextOfKins(String tagNo);
	
}
