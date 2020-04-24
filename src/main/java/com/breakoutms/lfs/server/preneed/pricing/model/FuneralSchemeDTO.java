package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.breakoutms.lfs.server.preneed.pricing.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.Premium;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class FuneralSchemeDTO {
	
	@NotBlank
	@Size(min = 1, max = 25)
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
