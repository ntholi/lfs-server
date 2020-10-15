package com.breakoutms.lfs.server.mortuary.released;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpse;


@Repository
public interface ReleasedCorpseRepository extends JpaRepository<ReleasedCorpse, Integer>, JpaSpecificationExecutor<ReleasedCorpse>{

	Optional<ReleasedCorpse> findByCorpse(Corpse corpse);

}
