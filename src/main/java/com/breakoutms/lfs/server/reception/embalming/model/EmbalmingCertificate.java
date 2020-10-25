package com.breakoutms.lfs.server.reception.embalming.model;

import com.breakoutms.lfs.common.enums.EvidenceOfDisease;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmbalmingCertificate {
	private String hair;
	private String beard;
	private String eyes;
	private String teeth;
	private String cosmetics;
	private String otherDescriptions;
	private EvidenceOfDisease evidenceOfDisease;
	private Boolean evidenceOfSurgery;
	private Boolean dropsical;
	private Boolean tissueGas;
	private Boolean externalWounds;
	private Boolean eruptions;
	private Boolean ulcerations;
	private Boolean purge;
	private Boolean mutilations;
	private Boolean rigorMortis;
	private String autopsyType;
	private String authorizedBy;
}
