package com.breakoutms.lfs.server.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.breakoutms.lfs.server.user.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	
    Optional<User> findByUsername(String username);

}