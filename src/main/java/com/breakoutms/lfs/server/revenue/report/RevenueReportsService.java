package com.breakoutms.lfs.server.revenue.report;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.products.model.QProduct;
import com.breakoutms.lfs.server.reports.Report;
import com.breakoutms.lfs.server.revenue.model.QRevenue;
import com.breakoutms.lfs.server.sales.model.QQuotation;
import com.breakoutms.lfs.server.sales.model.QSalesProduct;
import com.breakoutms.lfs.server.sales.report.SalesProductReport;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RevenueReportsService {
	
	private final EntityManager entityManager;
	
	public Map<String, Object> getProductSummaryReport(LocalDate from, LocalDate to, Integer branch, Integer userId){
		QSalesProduct salesProduct = QSalesProduct.salesProduct;
		QProduct product = QProduct.product;
		
		var  res =  new JPAQuery<>(entityManager)
				.from(salesProduct)
				.innerJoin(salesProduct.product, product)
				.groupBy(product)
				.orderBy(product.productType.asc())
				.select(Projections.constructor(ProductSummaryReport.class, 
						product.name, product.productType, salesProduct.quantity.sum(),
						salesProduct.cost.sum()))
				.fetch();
		return new Report<>(res).getContent();
	}
	
	public Map<String, Object> getCollectionsReport(LocalDate from, LocalDate to, Integer branch, Integer userId){
		QRevenue revenue = QRevenue.revenue;
		var  res =  new JPAQuery<>(entityManager)
				.from(revenue)
				.select(Projections.constructor(RevenueUser.class, 
						revenue.createdBy.firstName, revenue.amountPaid.sum()))
				.groupBy(revenue.createdBy)
				.fetch();
		
		return new Report<>(res).getContent();
	}

	@SuppressWarnings("unchecked")
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
								revenue.receiptNo,
								salesProduct.cost, salesProduct.quantity)))));
		
		
		Map<String, Object> report = new HashMap<>();
		report.put(Report.DATA_KEY, new ArrayList<>());
		report.put("salesProducts", new ArrayList<>());
		//TODO: OBVIOUSLY, THIS ADDS CPU/MEMORY EXTRA OVERHEAD 
		for(var it : res.values()) {
			ArrayList<Object> receipts = (ArrayList<Object>) report.get(Report.DATA_KEY);
			ArrayList<Object> salesProducts = (ArrayList<Object>) report.get("salesProducts");
			receipts.add(it.getReceipt());
			salesProducts.addAll(it.getSalesProducts());
		}
		
		return report;
	}
}
