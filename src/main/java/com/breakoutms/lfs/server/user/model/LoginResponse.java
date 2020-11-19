package com.breakoutms.lfs.server.user.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

	private String accessToken;
	private String tokenType;
	private List<String> updatableBeans;
	private boolean resetPassword;
}
