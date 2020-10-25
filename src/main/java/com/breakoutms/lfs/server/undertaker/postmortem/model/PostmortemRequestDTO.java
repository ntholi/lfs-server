package com.breakoutms.lfs.server.undertaker.postmortem.model;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.RequestPerson;
import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Relation(collectionRelation = "postmortemRequests")
public class PostmortemRequestDTO extends MortuaryRequestDTO<PostmortemRequestDTO>{

	private RequestPerson requestedBy;
	private String requestPerson;
	private String phoneNumber;
	@NotNull
	private String location;
}
