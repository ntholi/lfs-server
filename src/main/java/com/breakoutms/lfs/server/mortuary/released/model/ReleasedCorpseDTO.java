package com.breakoutms.lfs.server.mortuary.released.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.ReleasedCorpseStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "releasedCorpses")
public class ReleasedCorpseDTO extends RepresentationModel<ReleasedCorpseDTO>{

	private Integer id;
	private Integer burialDetailsId;
	@NotBlank
	private String tagNo;
	private LocalDateTime leavingTime;
	@NotNull
	private LocalDateTime date;
	private String dressedBy;
	private String coffinedBy;
	private ReleasedCorpseStatus status;
}
