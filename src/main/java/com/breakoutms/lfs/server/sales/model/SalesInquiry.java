package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor	
public class SalesInquiry {

	private String policyNumber;	
	private String tagNo;
	private String name;
	private String specialRequirements;
	private String dependentId;
	private BigDecimal payout;
	private Integer quotationNo;
	private List<SalesProductDTO> salesProducts;
}
