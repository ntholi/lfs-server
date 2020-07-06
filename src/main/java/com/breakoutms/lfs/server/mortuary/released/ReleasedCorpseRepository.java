package com.breakoutms.lfs.server.mortuary.released;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpse;


@Repository
public interface ReleasedCorpseRepository extends JpaRepository<ReleasedCorpse, Integer>{

}
