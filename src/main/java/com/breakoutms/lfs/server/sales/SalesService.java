package com.breakoutms.lfs.server.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.sales.model.BurialDetails;
import com.breakoutms.lfs.server.sales.model.Customer;
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
	public Sales save(final Sales sales, List<SalesProduct> salesProducts, 
			Customer customer, BurialDetails burialDetails) {
		
		Quotation quotation = new Quotation();
		quotation.setCustomer(customer);
		quotation.setSalesProducts(salesProducts);
		
		sales.setBurialDetails(burialDetails);
		sales.setQuotation(quotation);
		
		return repo.save(sales);
	}
	
	@Transactional
	public Sales update(Integer id, Sales entity) {
		if(entity == null) {
			throw ExceptionSupplier.nullUpdate("Sales").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Sales", id).get();
		}
		entity.setId(id);
		return repo.save(entity);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
