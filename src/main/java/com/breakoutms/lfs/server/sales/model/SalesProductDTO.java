package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "salesProducts")
public class SalesProductDTO extends RepresentationModel<SalesProductDTO> {

	private Long id;

	@NotNull
	private Integer productId;
	
	private String productName;
	
	private BigDecimal unitPrice;
	
	private int quantity;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	private BigDecimal cost;
	
	private boolean undeletable = true;
}
