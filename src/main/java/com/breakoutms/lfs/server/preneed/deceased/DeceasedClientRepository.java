package com.breakoutms.lfs.server.preneed.deceased;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;

@Repository
public interface DeceasedClientRepository extends JpaRepository<DeceasedClient, Long>{

}
