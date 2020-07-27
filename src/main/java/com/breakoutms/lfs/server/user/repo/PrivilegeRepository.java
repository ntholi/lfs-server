package com.breakoutms.lfs.server.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.common.enums.PrivilegeType;
import com.breakoutms.lfs.server.user.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer>{

	Optional<Privilege> findByType(PrivilegeType type);
}
