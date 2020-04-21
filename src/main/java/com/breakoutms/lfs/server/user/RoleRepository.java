package com.breakoutms.lfs.server.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
    Optional<Role> findByRoleName(String name);

}
