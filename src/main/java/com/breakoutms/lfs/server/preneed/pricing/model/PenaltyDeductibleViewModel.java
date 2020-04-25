package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "penaltyDeductibles")
public class PenaltyDeductibleViewModel extends RepresentationModel<PremiumViewModel>{

	private Integer id;
	private int months;
	private BigDecimal amount;
}
