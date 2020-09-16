package com.breakoutms.lfs.server.user.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.PrivilegeType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "privileges")
public class PrivilegeViewModel extends RepresentationModel<PrivilegeViewModel>  {
	
    private Integer id;
    private PrivilegeType type;
}
