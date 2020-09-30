package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.PolicyPaymentType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "policyPaymentDetails")
public class PolicyPaymentDetailsDTO extends RepresentationModel<PolicyPaymentDetailsDTO> {
	
	private Long id;
	
	private String policyNumber;
	
	@NotNull
	private PolicyPaymentType type;
	
	private Period period;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	private BigDecimal amount;
}
