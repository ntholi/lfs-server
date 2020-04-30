package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.server.preneed.model.Policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class PolicyPaymentDTO {
	
	private Long id;
	
	@NotNull
	private Policy policy;
	
	@NotNull
	private LocalDateTime paymentDate;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	private BigDecimal amountTendered;

	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 5, fraction = 2)
	private BigDecimal change;

	private List<PolicyPaymentDetails> policyPaymentInfo;
}
