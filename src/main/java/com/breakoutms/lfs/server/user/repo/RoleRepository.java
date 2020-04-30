package com.breakoutms.lfs.server.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.user.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Role> findByName(String name);
}
