package com.breakoutms.lfs.server.revenue.model;

import java.math.BigDecimal;
import java.util.List;

import com.breakoutms.lfs.server.sales.model.SalesProductDTO;

import lombok.Data;

@Data
public class RevenueInquiry {

	private String corpse;
	private String customerNames;
	private List<SalesProductDTO> salesProducts;
	private BigDecimal balance;
}
