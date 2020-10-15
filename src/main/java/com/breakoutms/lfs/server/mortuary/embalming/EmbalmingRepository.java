package com.breakoutms.lfs.server.mortuary.embalming;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.embalming.model.Embalming;


@Repository
public interface EmbalmingRepository extends JpaRepository<Embalming, Integer>, JpaSpecificationExecutor<Embalming>{

	List<Embalming> findAllByCorpse(Corpse corpse);

}
