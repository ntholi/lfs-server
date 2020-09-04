package com.breakoutms.lfs.server.sales.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SalesProductReport {

	private String productName;
	private BigDecimal cost;
	private int quantity;
	private Integer receiptNo;
	
	public SalesProductReport(String productName, Integer receiptNo, BigDecimal cost, int quantity) {
		this.productName = productName;
		this.receiptNo = receiptNo;
		this.cost = cost;
		this.quantity = quantity;
	}
	
}
