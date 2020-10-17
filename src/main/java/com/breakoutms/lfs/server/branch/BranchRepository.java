package com.breakoutms.lfs.server.branch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.branch.model.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer>, JpaSpecificationExecutor<Branch>{

	Optional<Branch> findByName(String name);

	@Query("select name from Branch")
	List<String> findAllBranchNames();

	@Query("select max(syncNumber) from Branch")
	Short getLastSyncNumber();

}
