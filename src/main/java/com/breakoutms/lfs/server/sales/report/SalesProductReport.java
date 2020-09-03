package com.breakoutms.lfs.server.sales.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SalesProductReport {

//	private String productName;

	private BigDecimal cost;

	private int quantity;
}
