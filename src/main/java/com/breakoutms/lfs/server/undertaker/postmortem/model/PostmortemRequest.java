package com.breakoutms.lfs.server.undertaker.postmortem.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.RequestPerson;
import com.breakoutms.lfs.common.enums.RequestType;
import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data 
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@PrimaryKeyJoinColumn(name = "mortuary_request_id")
public class PostmortemRequest extends MortuaryRequest {
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('POLICE', 'RELATIVES','OTHER')")
	private RequestPerson requestedBy;
	
	@Column(length = 50)
	private String requestPerson;
	
	@Column(length = 20)
	private String phoneNumber;
	
	@NotBlank
	@Size(max = 50)
	@Column(length = 50) 
	//nullable=false is leftout because UndertakerRequest uses SINGLE_TABLE strategy
	private String location;
	
	public String requestDetails() {
		StringBuilder sb = new StringBuilder();
		if(requestedBy != null) {
			sb.append(requestedBy);
		}
		if(requestedBy != null && requestPerson != null) {
			sb.append(" (");
		}
		if(requestPerson != null) {
			sb.append(requestPerson);
		}
		if(requestedBy != null && requestPerson != null) {
			sb.append(")");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return requestDetails();
	}

	@Override
	public RequestType getRequestType() {
		return RequestType.POSTMORTEM;
	}
}
