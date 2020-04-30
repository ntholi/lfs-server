package com.breakoutms.lfs.server.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

	private String accessToken;
	private String tokenType;
}
