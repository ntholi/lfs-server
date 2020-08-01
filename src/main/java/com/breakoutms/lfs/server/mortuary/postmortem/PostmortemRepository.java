package com.breakoutms.lfs.server.mortuary.postmortem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.mortuary.postmortem.model.Postmortem;

public interface PostmortemRepository extends JpaRepository<Postmortem, Integer> {

}
