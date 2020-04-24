package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "funeralSchemes")
public class FuneralSchemeViewModel extends RepresentationModel<FuneralSchemeViewModel> {

	private Integer id;
	private String name;
	private BigDecimal registrationFee;
	private int monthsBeforeActive;
	private boolean includesFirstPremium;
	private BigDecimal penaltyFee;
	private int monthsBeforePenalty;
}
