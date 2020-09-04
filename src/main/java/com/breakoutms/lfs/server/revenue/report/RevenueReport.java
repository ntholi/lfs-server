package com.breakoutms.lfs.server.revenue.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.breakoutms.lfs.server.sales.report.SalesProductReport;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RevenueReport {

	private Integer receiptNo;
	private Integer quotationNo;
	private BigDecimal amountPaid;
	private BigDecimal balance;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime date;
	private List<SalesProductReport> salesProducts;

	public RevenueReport(Integer receiptNo, Integer quotationNo, BigDecimal amountPaid, BigDecimal balance,
			LocalDateTime date, List<SalesProductReport> salesProducts) {
		this.receiptNo = receiptNo;
		this.quotationNo = quotationNo;
		this.amountPaid = amountPaid;
		this.balance = balance;
		this.date = date;
		this.salesProducts = salesProducts;
	}
}
