package com.breakoutms.lfs.server.products.model;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.TransportType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "transportPrices")
public class TransportPriceViewModel extends ProductViewModel{

	private TransportType transportType;

	private String from;
	
	private String to;
}
