package com.breakoutms.lfs.server.user.model;

import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.common.enums.Domain;

import lombok.Data;

@Data
public class RoleDTO {

    @Id
    private Integer id;
    
    @NotNull
    private Domain name;
 
    private List<Privilege> privileges;
}