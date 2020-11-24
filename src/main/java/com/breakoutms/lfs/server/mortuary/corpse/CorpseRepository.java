package com.breakoutms.lfs.server.mortuary.corpse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.mortuary.corpse.model.NextOfKin;

import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

@Repository
public interface CorpseRepository extends JpaRepository<Corpse, String>, JpaSpecificationExecutorWithProjection<Corpse>{

	@Query("FROM NextOfKin WHERE corpse.tagNo = :tagNo")
	List<NextOfKin> findNextOfKins(String tagNo);

	@Query("select c.tagNo as tagNo, "
			+ "c.names as names, "
			+ "c.surname as surname, "
			+ "c.arrivalDate as arrivalDate"
			+ " from Corpse c where CONCAT(c.names, ' ', c.surname) like %:fullnames%"
			+ " OR CONCAT(c.surname, ' ', c.names) like %:fullnames%")
	List<CorpseLookupProjection> lookup(@Param("fullnames") String fullnames);
}
