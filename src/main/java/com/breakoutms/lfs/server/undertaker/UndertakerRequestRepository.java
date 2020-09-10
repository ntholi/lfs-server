package com.breakoutms.lfs.server.undertaker;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;

public interface UndertakerRequestRepository extends JpaRepository<UndertakerRequest, Integer>, JpaSpecificationExecutor<UndertakerRequest>{

	Page<UndertakerRequest> findByProcessed(Pageable pageable, boolean processed);
	
	@Query("from UndertakerRequest where processed = false "
			+ " AND (CONCAT(corpse.names, ' ', corpse.surname) "
			+ " like %:fullnames% "
			+ " OR CONCAT(corpse.surname, ' ', corpse.names) like %:fullnames%)")
	List<UndertakerRequest> lookup(@Param("fullnames") String fullnames);

}
