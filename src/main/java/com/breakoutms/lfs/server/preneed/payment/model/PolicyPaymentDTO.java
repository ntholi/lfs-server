package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "policyPayments")
public class PolicyPaymentDTO extends RepresentationModel<PolicyPaymentDTO> {
	
	private Long id;
	
	private String policyNumber;
	
	@NotNull
	private LocalDateTime paymentDate;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	private BigDecimal amountTendered;

	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 5, fraction = 2)
	private BigDecimal change;

	private Set<PolicyPaymentDetailsDTO> policyPaymentDetails;
}
