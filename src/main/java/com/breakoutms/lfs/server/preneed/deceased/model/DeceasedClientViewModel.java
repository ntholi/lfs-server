package com.breakoutms.lfs.server.preneed.deceased.model;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@Relation(collectionRelation = "policyPaymentDetails")
public class DeceasedClientViewModel extends RepresentationModel<DeceasedClientViewModel> {
	
	private Long id;

	private String policyNumber;
	
	private String tagNo;
	
	private String dependentId;
	
	@Digits(integer = 8, fraction = 2)
	private BigDecimal payout;

}
