package com.breakoutms.lfs.server.user.model;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "updatableBeans")
public class UpdatableBeanDTO extends RepresentationModel<UpdatableBeanDTO> {
	
    private Integer id;
	@NotNull
	private String bean;
	@NotNull
	private String field;
	
	private Integer userId;
}
