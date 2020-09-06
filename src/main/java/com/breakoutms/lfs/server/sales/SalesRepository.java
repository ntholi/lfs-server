package com.breakoutms.lfs.server.sales;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer>{

	@Query("from SalesProduct where quotation.id = :quotationNo")
	List<SalesProduct> getSalesProducts(int quotationNo);

	List<Sales> findByQuotationId(Integer quotationNo, Sort sort);

}
