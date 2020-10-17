package com.breakoutms.lfs.server.branch.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "branches")
public class BranchDTO extends RepresentationModel<BranchDTO> {

	private Integer id;
	
	@NotBlank
	private String name;
	
	private String district;
	
	@Digits(integer = 4, fraction = 0)
	private Integer syncNumber;
}
