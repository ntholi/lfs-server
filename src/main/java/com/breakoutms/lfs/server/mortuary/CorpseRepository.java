package com.breakoutms.lfs.server.mortuary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.mortuary.model.Corpse;
import com.breakoutms.lfs.server.products.model.Product;

@Repository
public interface CorpseRepository extends JpaRepository<Corpse, String>{

}
