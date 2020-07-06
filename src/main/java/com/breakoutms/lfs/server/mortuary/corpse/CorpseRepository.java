package com.breakoutms.lfs.server.mortuary.corpse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.NextOfKin;

@Repository
public interface CorpseRepository extends JpaRepository<Corpse, String>{

	@Query("FROM NextOfKin WHERE corpse.tagNo = :tagNo")
	List<NextOfKin> findNextOfKins(String tagNo);
}
