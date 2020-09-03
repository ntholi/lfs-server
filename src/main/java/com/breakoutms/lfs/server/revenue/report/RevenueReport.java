package com.breakoutms.lfs.server.revenue.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.sales.report.SalesProductReport;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RevenueReport {

	private Integer quotationId;
	private BigDecimal amountPaid;
	private BigDecimal balance;
	private LocalDateTime date;
	private List<SalesProductReport> salesProducts;

	public RevenueReport(Integer quotationId, BigDecimal amountPaid, BigDecimal balance,
			LocalDateTime date, List<SalesProductReport> salesProducts) {
		this.quotationId = quotationId;
		this.amountPaid = amountPaid;
		this.balance = balance;
		this.date = date;
		this.salesProducts = salesProducts;
	}
}
