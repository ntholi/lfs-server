package com.breakoutms.lfs.server.branch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer>{

	Optional<Branch> findByName(String name);

	@Query("select name from Branch")
	List<String> findAllBranchNames();

}
