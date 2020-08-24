package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesInquiry {

	private String policyNumber;	
	private String tagNo;
	private String name;
	private String dependentId;
	private BigDecimal payout;
}
