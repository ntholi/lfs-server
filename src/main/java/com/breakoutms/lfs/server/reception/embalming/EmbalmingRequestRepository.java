package com.breakoutms.lfs.server.reception.embalming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.breakoutms.lfs.server.reception.embalming.model.EmbalmingRequest;

public interface EmbalmingRequestRepository extends JpaRepository<EmbalmingRequest, Integer>, JpaSpecificationExecutor<EmbalmingRequest>{

}
