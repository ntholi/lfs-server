package com.breakoutms.lfs.server.undertaker.transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;

public interface TransferRequestRepository extends JpaRepository<TransferRequest, Integer> {

}
