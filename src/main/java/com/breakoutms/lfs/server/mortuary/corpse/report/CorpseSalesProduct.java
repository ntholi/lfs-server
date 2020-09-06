package com.breakoutms.lfs.server.mortuary.corpse.report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CorpseSalesProduct {

	@JsonIgnore 
	private Long id;
	private String tagNo;
	private String productName;
	private int quantity;
	private BigDecimal cost;
}
