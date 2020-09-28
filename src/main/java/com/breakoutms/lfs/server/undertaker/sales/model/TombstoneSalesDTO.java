package com.breakoutms.lfs.server.undertaker.sales.model;


import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.sales.model.SalesProductViewModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "tombstoneSales")
public class TombstoneSalesDTO extends RepresentationModel<TombstoneSalesDTO> {

	private Integer id;
	private Integer quotationNo;
	private String tagNo;
	private List<SalesProductViewModel> salesProducts;
}
