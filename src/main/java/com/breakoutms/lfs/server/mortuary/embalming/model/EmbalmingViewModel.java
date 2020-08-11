package com.breakoutms.lfs.server.mortuary.embalming.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.EmbalmingType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "embalmings")
public class EmbalmingViewModel extends RepresentationModel<EmbalmingViewModel>{

	private Integer id;
	private String tagNo;
	private LocalDateTime date;
	private String embalmer;
	private EmbalmingType embalmingType;
	private double formalin;
}
