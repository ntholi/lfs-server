package com.breakoutms.lfs.server.user.model;

import lombok.Data;

@Data
public class PasswordDTO {

	private String oldPassword;
	private String password;
	private String confirmPassword;
}
