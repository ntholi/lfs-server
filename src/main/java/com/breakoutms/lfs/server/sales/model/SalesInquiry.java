package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesInquiry {

	private String policyNumber;	
	private String tagNo;
	private String name;
	private String dependentId;
	private BigDecimal payout;
}
