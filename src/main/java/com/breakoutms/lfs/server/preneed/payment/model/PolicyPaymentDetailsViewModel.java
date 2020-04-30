package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class PolicyPaymentDetailsViewModel extends RepresentationModel<PolicyPaymentDetailsViewModel> {

	private Long id;
	private PolicyPaymentDetails.Type type;
	private Period period;
	private BigDecimal amount;
	private PolicyPayment policyPayment;
	private boolean markedAsPaid;
}
