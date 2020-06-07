package com.breakoutms.lfs.server.products.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "products")
public class ProductViewModel extends RepresentationModel<ProductViewModel> {

	private Integer id;

	private String name;
	
	private BigDecimal price;
	
	private ProductType productType;
	
	private String description;
}
