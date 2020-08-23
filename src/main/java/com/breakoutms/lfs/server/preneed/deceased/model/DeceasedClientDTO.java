package com.breakoutms.lfs.server.preneed.deceased.model;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class DeceasedClientDTO{
	
	private Long id;

	private String policyNumber;
	
	private String tagNo;
	
	private boolean isDependent;
	
	private String dependentId;
	
	@Digits(integer = 8, fraction = 2)
	private BigDecimal payout;
}
