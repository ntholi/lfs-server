package com.breakoutms.lfs.server.revenue;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Integer>, JpaSpecificationExecutor<Revenue>{

	@Query("from SalesProduct where quotation.id = :quotationNo")
	List<SalesProduct> getRevenueProducts(int quotationNo);
	
	List<Revenue> findByQuotationId(Integer id, Sort sort);

}
