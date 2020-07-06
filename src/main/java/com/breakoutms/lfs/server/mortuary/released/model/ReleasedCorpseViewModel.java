package com.breakoutms.lfs.server.mortuary.released.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "releasedCorpses")
public class ReleasedCorpseViewModel extends RepresentationModel<ReleasedCorpseViewModel>{

	private Integer id;
	private String tagNo;
	private LocalDateTime date;
	private String dressedBy;
	private String coffinedBy;
}
