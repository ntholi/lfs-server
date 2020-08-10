package com.breakoutms.lfs.server.undertaker;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;

public interface UndertakerRequestRepository extends JpaRepository<UndertakerRequest, Integer>{

}
