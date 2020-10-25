package com.breakoutms.lfs.server.mortuary.request;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequest;

public interface MortuaryRequestRepository extends JpaRepository<MortuaryRequest, Integer>, JpaSpecificationExecutor<MortuaryRequest>{

	Page<MortuaryRequest> findByProcessed(Pageable pageable, boolean processed);
	
	@Query("from MortuaryRequest where processed = false "
			+ " AND (CONCAT(corpse.names, ' ', corpse.surname) "
			+ " like %:fullnames% "
			+ " OR CONCAT(corpse.surname, ' ', corpse.names) like %:fullnames%)")
	List<MortuaryRequest> lookup(@Param("fullnames") String fullnames);

}
