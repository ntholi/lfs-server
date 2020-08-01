package com.breakoutms.lfs.server.undertaker.postmortem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;

public interface PostmortemRequestRepository extends JpaRepository<PostmortemRequest, Long> {

}
