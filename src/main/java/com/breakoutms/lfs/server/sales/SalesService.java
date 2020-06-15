package com.breakoutms.lfs.server.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalesService {

	private final SalesRepository repo;

	public Optional<Sales> get(Integer id) {
		return repo.findById(id);
	}

	public Page<Sales> all(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Transactional
	public Sales save(final Sales sales) {
		setAssociations(sales);
		return repo.save(sales);
	}

	@Transactional
	public Sales update(Integer id, Sales updateEntity) {
		if(updateEntity == null) {
			throw ExceptionSupplier.nullUpdate("Sales").get();
		}
		Sales entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Sales", id));

		setAssociations(updateEntity);
		SalesMapper.INSTANCE.update(updateEntity, entity);

		return repo.save(entity);
	}

	protected void setAssociations(final Sales sales) {
		Quotation quot = sales.getQuotation();
		if(quot != null) {
			List<SalesProduct> salesProducts = quot.getSalesProducts();
			if(salesProducts != null) {
				salesProducts.forEach(it -> it.setQuotation(quot));
			}
		}
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public List<SalesProduct> getSalesProducts(Integer quotationNo) {
		return repo.getSalesProducts(quotationNo);
	}
}
