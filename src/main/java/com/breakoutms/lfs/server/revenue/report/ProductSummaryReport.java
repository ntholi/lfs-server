package com.breakoutms.lfs.server.revenue.report;

import java.math.BigDecimal;

import com.breakoutms.lfs.common.enums.ProductType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryReport {

	private String productName;
	private ProductType productType;
	private int quantity;
	private BigDecimal amount;
}
