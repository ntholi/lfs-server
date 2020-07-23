package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.common.PolicyPaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PolicyPaymentDetailsDTO {
	
	@NotNull
	private PolicyPaymentType type;
	
	private Period period;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	private BigDecimal amount;
}
