package com.breakoutms.lfs.server.user.model;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "users")
public class UserViewModel extends RepresentationModel<UserViewModel> {

	private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String branchName;
    private List<RoleViewModel> roles;
    
    public String getPassword() {
    	return "********";
    }
}
