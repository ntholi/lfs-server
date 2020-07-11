package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.core.enums.PolicyPaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "policyPaymentDetails")
public class PolicyPaymentDetailsViewModel extends RepresentationModel<PolicyPaymentDetailsViewModel> {

	private Long id;
	private PolicyPaymentType type;
	private Period period;
	private BigDecimal amount;
}
