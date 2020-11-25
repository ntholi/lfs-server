package com.breakoutms.lfs.server.mortuary.postmortem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.postmortem.model.Postmortem;

import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

public interface PostmortemRepository extends JpaRepository<Postmortem, Integer>, JpaSpecificationExecutorWithProjection<Postmortem> {

	Iterable<Postmortem> findAllByCorpse(Corpse corpse);

}
