package com.breakoutms.lfs.server.mortuary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.model.OtherMortuary;

@Repository
public interface OtherMortuaryRepository extends JpaRepository<OtherMortuary, Integer>{
	
	Optional<OtherMortuary> findFirstByName(String name);
}
