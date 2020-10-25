package com.breakoutms.lfs.server.reception.embalming.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.EvidenceOfDisease;
import com.breakoutms.lfs.server.undertaker.model.UndertakerRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "embalmingRequests")
public class EmbalmingRequestDTO extends UndertakerRequestDTO<EmbalmingRequestDTO>{

	@Length(max = 50)
	private String hair;
	@Length(max = 50)
	private String beard;
	@Length(max = 50)
	private String eyes;
	@Length(max = 50)
	private String teeth;
	@Length(max = 50)
	private String cosmetics;
	@Length(max = 150)
	private String otherDescriptions;
	private EvidenceOfDisease evidenceOfDisease;
	private String evidenceOfSurgery;
	private String dropsical;
	private String tissueGas;
	private String externalWounds;
	private String eruptions;
	private String ulcerations;
	private String purge;
	private String mutilations;
	private String rigorMortis;
	private String postMortemPigmentation;
	private String autopsyType;
	
	@Length(max = 150)
	private String authorizedBy;
}
