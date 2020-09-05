package com.breakoutms.lfs.server.mortuary.corpse.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CorpseSalesProduct {

	private String tagNo;
	private String productName;
	private BigDecimal cost;
	private int quantity;
}
