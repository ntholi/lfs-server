package com.breakoutms.lfs.server.undertaker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;

@Repository
public interface UndertakerRequestRepository<T extends UndertakerRequest> extends JpaRepository<T, Long>{

}
