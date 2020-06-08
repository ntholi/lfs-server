package com.breakoutms.lfs.server.preneed.pricing.model;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.products.model.ProductViewModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "coffins")
public class CoffinViewModel extends ProductViewModel{

	private String category;
}
