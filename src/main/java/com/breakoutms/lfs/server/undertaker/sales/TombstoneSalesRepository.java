package com.breakoutms.lfs.server.undertaker.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.undertaker.sales.model.TombstoneSales;

@Repository
public interface TombstoneSalesRepository extends JpaRepository<TombstoneSales, Integer>, JpaSpecificationExecutor<TombstoneSales>{

	@Query("from SalesProduct where quotation.id = :quotationNo")
	List<SalesProduct> getSalesProducts(int quotationNo);

	List<TombstoneSales> findByQuotationId(Integer quotationNo, Sort sort);

	@Query("from Sales where quotation.corpse.tagNo = :tagNo")
	Optional<TombstoneSales> findByCorpseTagNo(String tagNo);

}
