package com.breakoutms.lfs.server.user.model;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "roles")
public class RoleViewModel extends RepresentationModel<RoleViewModel>  {
	
    private Integer id;
    private Domain name;
    private List<PrivilegeDTO> privileges;
}

