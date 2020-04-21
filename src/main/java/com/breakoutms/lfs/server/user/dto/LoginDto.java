package com.breakoutms.lfs.server.user.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
