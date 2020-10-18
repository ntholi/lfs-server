package com.breakoutms.lfs.server.reception.embalming.model;

import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClientDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "embalmingRequests")
public class EmbalmingRequestDTO extends RepresentationModel<DeceasedClientDTO>{

	private Integer id;
	@NotBlank
	private String tagNo;
	private String authorizedBy;
}
