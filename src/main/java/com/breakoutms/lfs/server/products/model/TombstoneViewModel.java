package com.breakoutms.lfs.server.products.model;

import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "tombstonePrices")
public class TombstoneViewModel extends ProductViewModel{

	private String category;
}
