package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit.Deductable;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "funeralSchemeBenefits")
public class FuneralSchemeBenefitDTO extends RepresentationModel<FuneralSchemeBenefitDTO> {

	private Integer id;
	private ProductType productType;
	private Deductable deductable;
	private BigDecimal discount;
}
