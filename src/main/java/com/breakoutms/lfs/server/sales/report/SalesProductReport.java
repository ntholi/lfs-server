package com.breakoutms.lfs.server.sales.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SalesProductReport {

	private String productName;
	private BigDecimal cost;
	private int quantity;
	
	public SalesProductReport(String productName, BigDecimal cost, int quantity) {
		this.productName = productName;
		this.cost = cost;
		this.quantity = quantity;
	}
	
}
