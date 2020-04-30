package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.breakoutms.lfs.server.preneed.model.Policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data @Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor @NoArgsConstructor
public class PolicyPaymentViewModel extends RepresentationModel<PolicyPaymentViewModel> {
	
	private Long id;
	private Policy policy;
	private LocalDateTime paymentDate;
	private BigDecimal amountTendered;
	private BigDecimal change;
	private List<PolicyPaymentDetails> policyPaymentInfo;
}
