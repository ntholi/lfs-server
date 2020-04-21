package com.breakoutms.lfs.server.user.dto;

import java.util.List;

import com.breakoutms.lfs.server.user.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserDto {
	
    private String username;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private List<Role> roles;
}
