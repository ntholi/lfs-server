package com.breakoutms.lfs.server.user.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.common.enums.Privilege;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "roles")
public class RoleDTO extends RepresentationModel<UserDTO> {

    private Integer id;
    @NotNull
    private Domain name;
    private List<Privilege> privileges;
}