package com.breakoutms.lfs.server.user.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
