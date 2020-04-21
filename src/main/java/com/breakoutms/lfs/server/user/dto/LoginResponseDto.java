package com.breakoutms.lfs.server.user.dto;

import java.util.List;

import com.breakoutms.lfs.server.user.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

	private String username;
	private String fullNames;
	private List<Role> roles;
	private String accessToken;
	private String tokenType;
}
