package com.breakoutms.lfs.server.preneed;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.preneed.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, String>{

}
