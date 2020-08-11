package com.breakoutms.lfs.server.undertaker;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;

public interface UndertakerRequestRepository extends JpaRepository<UndertakerRequest, Integer>{

	@Query("from UndertakerRequest where CONCAT(corpse.names, ' ', corpse.surname) "
			+ " like %:fullnames% "
			+ " OR CONCAT(corpse.surname, ' ', corpse.names) like %:fullnames%")
	List<UndertakerRequest> lookup(@Param("fullnames") String fullnames);

}
