package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "dependentBenefits")
public class DependentBenefitDTO extends RepresentationModel<DependentBenefitDTO> {

	private Integer id;
	private int minimumAge;
	private int maximumAge;
	private BigDecimal coverAmount;
}
