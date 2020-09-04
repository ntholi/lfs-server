package com.breakoutms.lfs.server.revenue.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.breakoutms.lfs.server.sales.report.SalesProductReport;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RevenueReport {


	@AllArgsConstructor
	@Data
	public class Receipt {
		private Integer receiptNo;
		private Integer quotationNo;
		private BigDecimal amountPaid;
		private BigDecimal balance;
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		private LocalDateTime date;
	}
	
	private Receipt receipt;
	private List<SalesProductReport> salesProducts;

	public RevenueReport(Integer receiptNo, Integer quotationNo, BigDecimal amountPaid, BigDecimal balance,
			LocalDateTime date) {
		this.receipt = new Receipt(receiptNo, quotationNo, amountPaid, balance, date);
	}
	
	public RevenueReport(Integer receiptNo, Integer quotationNo, BigDecimal amountPaid, BigDecimal balance,
			LocalDateTime date, List<SalesProductReport> salesProducts) {
		this.receipt = new Receipt(receiptNo, quotationNo, amountPaid, balance, date);
		this.salesProducts = salesProducts;
	}
}
