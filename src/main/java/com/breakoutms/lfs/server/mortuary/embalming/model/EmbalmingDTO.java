package com.breakoutms.lfs.server.mortuary.embalming.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.EmbalmingType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "embalmings")
public class EmbalmingDTO extends RepresentationModel<EmbalmingDTO>{

	private Integer id;
	@NotBlank
	private String tagNo;
	@NotNull
	private LocalDateTime date;
	private String embalmer;
	private EmbalmingType embalmingType;
	private double formalin;
	@NotNull
	private Integer requestId;
}
