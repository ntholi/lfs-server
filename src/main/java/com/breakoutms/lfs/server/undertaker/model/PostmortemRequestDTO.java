package com.breakoutms.lfs.server.undertaker.model;

import com.breakoutms.lfs.common.enums.RequestPerson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostmortemRequestDTO {
	
	private Long id;
	private String tagNo;
	private RequestPerson requestedBy;
	private String requestPerson;
	private String phoneNumber;
	private String location;
}
