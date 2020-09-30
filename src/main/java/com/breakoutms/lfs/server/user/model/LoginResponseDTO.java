package com.breakoutms.lfs.server.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

	private String accessToken;
	private String tokenType;
}