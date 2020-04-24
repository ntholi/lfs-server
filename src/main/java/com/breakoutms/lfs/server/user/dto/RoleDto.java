package com.breakoutms.lfs.server.user.dto;

import java.util.stream.Collectors;

import com.breakoutms.lfs.server.user.Privilege;
import com.breakoutms.lfs.server.user.PrivilegeType;
import com.breakoutms.lfs.server.user.Role;
import com.breakoutms.lfs.server.user.Domain;

import lombok.Data;

@Data
public class RoleDto {
	private Domain name;
	private String privileges;
	
	public RoleDto(Role role) {
		name = role.getName();
		if(role.getPrivileges() != null) {
			privileges = role.getPrivileges()
				.stream()
				.map(Privilege::getType)
				.map(PrivilegeType::toString)
				.collect(Collectors.joining(","));
		}
	}

	public static String[] privilegesFromString(String privileges) {
		return privileges.split(",");
	}
}
