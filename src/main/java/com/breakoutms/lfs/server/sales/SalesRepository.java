package com.breakoutms.lfs.server.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer>, JpaSpecificationExecutorWithProjection<Sales>{

	@Query("from SalesProduct where quotation.id = :quotationNo")
	List<SalesProduct> getSalesProducts(int quotationNo);

	List<Sales> findByQuotationId(Integer quotationNo, Sort sort);

	@Query("from Sales where quotation.corpse.tagNo = :tagNo")
	Optional<Sales> findByCorpseTagNo(String tagNo);

}
