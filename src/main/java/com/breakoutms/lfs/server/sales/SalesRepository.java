package com.breakoutms.lfs.server.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.sales.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer>{

}
