package com.breakoutms.lfs.server.revenue.report;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RevenueUser {

	private String names;
	private BigDecimal amount;
	
	public RevenueUser(String names, BigDecimal amount) {
		this.names = names;
		this.amount = amount;
	}
	
	
}
