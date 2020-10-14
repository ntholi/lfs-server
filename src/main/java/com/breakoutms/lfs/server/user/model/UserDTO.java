package com.breakoutms.lfs.server.user.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "users")
public class UserDTO extends RepresentationModel<UserDTO> {

    private Integer id;
    
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

    private List<RoleDTO> roles;
    
    private List<UpdatableBeanDTO> updatableBeans;
    
    @NotBlank
    private String branchName;
    
	public String getFullName() {
	    return Stream.of(firstName, lastName)
	    		.filter(it -> it != null && !it.isBlank())
	    		.collect(Collectors.joining(" "));
	}
}
