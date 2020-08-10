package com.breakoutms.lfs.server.undertaker.model;

import org.springframework.hateoas.RepresentationModel;

import com.breakoutms.lfs.common.enums.RequestPerson;
import com.breakoutms.lfs.common.enums.RequestType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UndertakerRequestInquiry extends RepresentationModel<UndertakerRequestInquiry> {

	private Integer id;
	private String tagNo;
	private boolean seen;
	private boolean processed;
	private RequestPerson requestedBy;
	private String requestPerson;
	private String phoneNumber;
	private String location;
	private String transferTo;
	private RequestType requestType;
}
