package com.breakoutms.lfs.server.products.model;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.EmbalmingType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "embalmingPrices")
public class EmbalmingPriceModel extends ProductViewModel{

	private EmbalmingType embalmingType;
}
