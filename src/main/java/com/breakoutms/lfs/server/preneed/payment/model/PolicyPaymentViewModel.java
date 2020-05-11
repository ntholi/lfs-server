package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data @Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "policyPayments")
public class PolicyPaymentViewModel extends RepresentationModel<PolicyPaymentViewModel> {
	
	private Long id;
	private LocalDateTime paymentDate;
	private BigDecimal amountTendered;
	private BigDecimal change;
}
