package com.breakoutms.lfs.server.reception.embalming.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.EvidenceOfDisease;
import com.breakoutms.lfs.common.enums.RequestType;
import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequest;
import com.breakoutms.lfs.server.persistence.IdGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "embalming_request_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@PrimaryKeyJoinColumn(name = "mortuary_request_id")
public class EmbalmingRequest extends MortuaryRequest {

	@Column(length = 50)
	private String hair;
	@Column(length = 50)
	private String beard;
	@Column(length = 50)
	private String eyes;
	@Column(length = 50)
	private String teeth;
	@Column(length = 50)
	private String cosmetics;
	@Column(length = 150)
	private String otherDescriptions;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 11)
	private EvidenceOfDisease evidenceOfDisease;
	private Boolean evidenceOfSurgery;
	private Boolean dropsical;
	private Boolean tissueGas;
	private Boolean externalWounds;
	private Boolean eruptions;
	private Boolean ulcerations;
	@Column(name = "purge_fluid")
	private Boolean purge;
	private Boolean mutilations;
	private Boolean rigorMortis;
	private Boolean postMortemPigmentation;
	private String autopsyType;
	
	@Column(length = 50)
	private String authorizedBy;

	@Override
	public RequestType getRequestType() {
		return RequestType.EMBALMING;
	}
}
