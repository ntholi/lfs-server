package com.breakoutms.lfs.server.revenue.model;

import java.math.BigDecimal;
import java.util.List;

import com.breakoutms.lfs.server.sales.model.SalesProductViewModel;

import lombok.Data;

@Data
public class RevenueInquiry {

	private String corpse;
	private String customerNames;
	private List<SalesProductViewModel> salesProducts;
	private BigDecimal balance;
}
