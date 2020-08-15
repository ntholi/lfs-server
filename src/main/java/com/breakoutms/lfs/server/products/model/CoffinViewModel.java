package com.breakoutms.lfs.server.products.model;

import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "transportPrices")
public class CoffinViewModel extends ProductViewModel{

	private String category;
}
