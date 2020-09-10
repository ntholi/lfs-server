package com.breakoutms.lfs.server.preneed.deceased;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;

@Repository
public interface DeceasedClientRepository extends JpaRepository<DeceasedClient, Long>, JpaSpecificationExecutor<DeceasedClient>{

	Optional<DeceasedClient> findByCorpseTagNo(String tagNo);
}
