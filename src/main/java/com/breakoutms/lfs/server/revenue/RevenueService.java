package com.breakoutms.lfs.server.revenue;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.products.model.QProduct;
import com.breakoutms.lfs.server.reports.Report;
import com.breakoutms.lfs.server.revenue.model.QRevenue;
import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.revenue.model.RevenueInquiry;
import com.breakoutms.lfs.server.revenue.report.RevenueReport;
import com.breakoutms.lfs.server.sales.QuotationRepository;
import com.breakoutms.lfs.server.sales.SalesRepository;
import com.breakoutms.lfs.server.sales.model.QQuotation;
import com.breakoutms.lfs.server.sales.model.QSalesProduct;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.sales.report.SalesProductReport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RevenueService {

	private final RevenueRepository repo;
	private final SalesRepository salesRepo;
	private final QuotationRepository quotationRepository;
	private final EntityManager entityManager;

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
		var sales = salesRepo.findByQuotationId(quotationNo)
				.orElseThrow(ExceptionSupplier.notFound("Quotation", quotationNo));
		RevenueInquiry inquiry = new RevenueInquiry();
		var burial = sales.getBurialDetails();
		if(burial != null) {
			var corpse = burial.getCorpse();
			inquiry.setCorpse(corpse != null? corpse.getFullName() : "");
		}
		if(sales.getQuotation() != null) {
			var quotation = sales.getQuotation();
			var customer = quotation.getCustomer();
			inquiry.setCustomerNames(customer != null? customer.getNames() : "");
			inquiry.setSalesProducts(RevenueMapper.INSTANCE.map(quotation.getSalesProducts()));
			var balance = quotation.getSalesProducts()
					.stream()
					.map(SalesProduct::getCost)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			inquiry.setBalance(balance);
		}
		return inquiry;
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public List<SalesProduct> getRevenueProducts(Integer quotationNo) {
		return repo.getRevenueProducts(quotationNo);
	}

	public Map<String, Object> getRevenueReport(LocalDate from, LocalDate to, Integer branch, Integer user) {
		QRevenue revenue = QRevenue.revenue;
		QSalesProduct salesProduct = QSalesProduct.salesProduct;
		QQuotation quotation = QQuotation.quotation;
		QProduct product = QProduct.product;
		
		var  res =  new JPAQuery<>(entityManager)
				.from(revenue)
				.innerJoin(revenue.quotation, quotation)
				.innerJoin(quotation.salesProducts, salesProduct)
				.innerJoin(salesProduct.product, product)
				.transform(groupBy(revenue.receiptNo)
				.as(Projections.constructor(RevenueReport.class, revenue.receiptNo,
						revenue.quotation.id,
						revenue.amountPaid, revenue.balance, revenue.date, 
						list(Projections.constructor(SalesProductReport.class, salesProduct.product.name, 
								salesProduct.cost, salesProduct.quantity)))));
		
		return new Report<>(res.values()).getContent();
	}
}
