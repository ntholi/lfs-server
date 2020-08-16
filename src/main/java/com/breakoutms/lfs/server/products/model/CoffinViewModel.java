package com.breakoutms.lfs.server.products.model;

import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "coffinPrices")
public class CoffinViewModel extends ProductViewModel{

	private String category;
}
