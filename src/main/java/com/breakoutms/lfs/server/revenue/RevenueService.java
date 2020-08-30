package com.breakoutms.lfs.server.revenue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.revenue.model.RevenueInquiry;
import com.breakoutms.lfs.server.sales.QuotationRepository;
import com.breakoutms.lfs.server.sales.SalesRepository;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RevenueService {

	private final RevenueRepository repo;
	private final SalesRepository salesRepo;
	private final QuotationRepository quotationRepository;

	public Optional<Revenue> get(Integer id) {
		return repo.findById(id);
	}

	public Page<Revenue> all(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Transactional
	public Revenue save(final Revenue revenue) {
		setAssociations(revenue);
		return repo.save(revenue);
	}

	@Transactional
	public Revenue update(Integer id, Revenue updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Revenue").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Revenue", id));

		setAssociations(updatedEntity);
		RevenueMapper.INSTANCE.update(updatedEntity, entity);

		return repo.save(entity);
	}
	
	private void setAssociations(Revenue entity) {
		var qId = entity.getQuotation().getId();
		var quotation = quotationRepository.findById(qId)
				.orElseThrow(ExceptionSupplier.notFound("Quotation", qId));
		entity.setQuotation(quotation);
	}

	@Transactional(readOnly = true)
	public RevenueInquiry revenueInquiry(Integer quotationNo) {
		var sales = salesRepo.findByQuotationId(quotationNo)
				.orElseThrow(ExceptionSupplier.notFound("Quotation", quotationNo));
		RevenueInquiry result = new RevenueInquiry();
		var burial = sales.getBurialDetails();
		if(burial != null) {
			var corpse = burial.getCorpse();
			result.setCorpse(corpse != null? corpse.getFullName() : "");
		}
		var quot = sales.getQuotation();
		if(quot != null) {
			var customer = quot.getCustomer();
			result.setCustomerNames(customer != null? customer.getNames() : "");
			result.setSalesProducts(RevenueMapper.INSTANCE.map(quot.getSalesProducts()));
			var balance = quot.getSalesProducts()
					.stream()
					.map(SalesProduct::getCost)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			result.setBalance(balance);
		}
		return result;
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public List<SalesProduct> getRevenueProducts(Integer quotationNo) {
		return repo.getRevenueProducts(quotationNo);
	}
}
