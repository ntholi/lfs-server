package com.breakoutms.lfs.server.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.user.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer>{

	Optional<Privilege> findByType(Privilege.Type type);
}
