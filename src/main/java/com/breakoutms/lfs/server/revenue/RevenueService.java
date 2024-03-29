package com.breakoutms.lfs.server.revenue;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.revenue.model.RevenueInquiry;
import com.breakoutms.lfs.server.sales.QuotationRepository;
import com.breakoutms.lfs.server.sales.SalesRepository;
import com.breakoutms.lfs.server.sales.model.Quotation;
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
		quotation.addRevenue(entity);
	}

	@Transactional(readOnly = true)
	public RevenueInquiry revenueInquiry(Integer quotationNo) {
		Sort sort = Sort.by(Direction.DESC, "createdAt");
		List<Revenue> list = repo.findByQuotationId(quotationNo, sort);
		RevenueInquiry inquiry;
		if(!list.isEmpty()) {
			var revenue = list.get(0);
			inquiry = new RevenueInquiry();
			inquiry.setBalance(revenue.getBalance());
			Quotation quotation = revenue.getQuotation();
			if(quotation != null) {
				var customer = quotation.getCustomer();
				inquiry.setCustomerNames(customer != null? customer.getNames() : "");
				inquiry.setSalesProducts(RevenueMapper.INSTANCE.map(quotation.getSalesProducts()));
			}
		}
		else {
			inquiry = buildInquiryFromSales(quotationNo);
		}
		return inquiry;
	}

	private RevenueInquiry buildInquiryFromSales(Integer quotationNo) {
		Sort sort = Sort.by(Direction.DESC, "createdAt");
		var salesList = salesRepo.findByQuotationId(quotationNo, sort);
		if(salesList.isEmpty()) {
			throw ExceptionSupplier.notFound("Quotation", quotationNo).get();
		}
		var sales = salesList.get(0);
		RevenueInquiry inquiry = new RevenueInquiry();
		var corpse = sales.getCorpse();
		inquiry.setCorpse(corpse != null? corpse.getFullName() : "");
		
		var quotation = sales.getQuotation();
		if(quotation != null) {
			var customer = quotation.getCustomer();
			inquiry.setCustomerNames(customer != null? customer.getNames() : "");
			inquiry.setSalesProducts(RevenueMapper.INSTANCE.map(quotation.getSalesProducts()));
			inquiry.setBalance(sales.getPayableAmount());
		}
		return inquiry;
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public Page<Revenue> search(Specification<Revenue> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	public List<SalesProduct> getRevenueProducts(Integer quotationNo) {
		return repo.getRevenueProducts(quotationNo);
	}
}
