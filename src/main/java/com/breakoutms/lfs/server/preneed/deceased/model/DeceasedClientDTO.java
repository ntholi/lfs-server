package com.breakoutms.lfs.server.preneed.deceased.model;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "deceasedClients")
public class DeceasedClientDTO extends RepresentationModel<DeceasedClientDTO>{
	
	private Long id;

	private String policyNumber;
	
	private String tagNo;
	
	private String dependentId;
	
	@Digits(integer = 8, fraction = 2)
	private BigDecimal payout;
}
