package com.breakoutms.lfs.server.mortuary.transferout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.transferout.model.TransferOut;


@Repository
public interface TransferOutRepository extends JpaRepository<TransferOut, Integer>{

}
