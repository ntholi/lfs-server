package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FuneralSchemeDTO {
	
	private Integer id;
	
	@NotBlank
	@Size(min = 1, max = 35)
	private String name;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer=6, fraction=2)
	private BigDecimal registrationFee;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Max(255)
	private int monthsBeforeActive;
	
	private boolean includesFirstPremium;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer=6, fraction=2)
	private BigDecimal penaltyFee;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Max(255)
	private int monthsBeforePenalty;
	
	private List<Premium> premiums;
	private List<DependentBenefit> dependentBenefits;
	private List<FuneralSchemeBenefit> benefits;
	private List<PenaltyDeductible> penaltyDeductibles;
	
}
