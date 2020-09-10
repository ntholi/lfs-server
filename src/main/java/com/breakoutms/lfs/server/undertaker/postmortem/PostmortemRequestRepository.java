package com.breakoutms.lfs.server.undertaker.postmortem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;

public interface PostmortemRequestRepository extends JpaRepository<PostmortemRequest, Integer>, JpaSpecificationExecutor<PostmortemRequest> {

}
