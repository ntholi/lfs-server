package com.breakoutms.lfs.server.user.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    
    @Size(min = 2, max = 50)
    private String firstName;
    
    @Size(min = 2, max = 50)
    private String lastName;

    private List<Role> roles;
    
    @NotBlank
    private String branchName;
}
