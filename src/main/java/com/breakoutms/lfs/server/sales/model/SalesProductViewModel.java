package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "salesProducts")
public class SalesProductViewModel extends RepresentationModel<SalesProductViewModel> {

	private Long id;
	private String productName;
	private Integer productId;
	private BigDecimal unitPrice;
	private BigDecimal cost;
	private int quantity;	
	private boolean undeletable = true;
}
