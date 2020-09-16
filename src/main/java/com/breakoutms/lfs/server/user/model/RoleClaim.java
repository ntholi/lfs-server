package com.breakoutms.lfs.server.user.model;

import java.util.stream.Collectors;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.common.enums.PrivilegeType;

import lombok.Data;

@Data
public class RoleClaim {
	private Domain name;
	private String privileges;
	
	public RoleClaim(Role role) {
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
