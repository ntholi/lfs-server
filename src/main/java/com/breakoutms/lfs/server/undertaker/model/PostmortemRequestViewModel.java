package com.breakoutms.lfs.server.undertaker.model;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.undertaker.model.PostmortemRequest.RequestPerson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Relation(collectionRelation = "postmortemRequests")
public class PostmortemRequestViewModel extends UndertakerRequestViewModel<PostmortemRequestViewModel>{

	private RequestPerson requestedBy;
	private String requestPerson;
	private String phoneNumber;
	private String location;
}
