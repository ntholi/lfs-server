package com.breakoutms.lfs.server.mortuary.postmortem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.postmortem.model.Postmortem;

public interface PostmortemRepository extends JpaRepository<Postmortem, Integer>, JpaSpecificationExecutor<Postmortem> {

	Iterable<Postmortem> findAllByCorpse(Corpse corpse);

}
