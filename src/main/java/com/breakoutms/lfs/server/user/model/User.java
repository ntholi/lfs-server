package com.breakoutms.lfs.server.user.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor @NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "username", name = "unique_username", unique=true)
})
public class User extends AuditableEntity<Integer>{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "SMALLINT UNSIGNED")
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false, length = 50)
    private String username;

    @JsonIgnore
    @Size(min = 6, max = 100)
    @Column(length = 100)
    private String password;
    
    @Size(min = 2, max = 50)
    @Column(length = 50)
    private String firstName;
    
    @Size(min = 2, max = 50)
    @Column(length = 50)
    private String lastName;

	@OneToMany(mappedBy="user", 
			cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles;
	
	public void setRoles(List<Role> roles) {
		if(this.roles == null) {
			this.roles = new ArrayList<>();
		}
		this.roles.clear();
		if(roles != null) {
			roles.forEach(it -> it.setId(null));
			this.roles.addAll(roles);
		}
	}
	
	public String getFullName() {
	    return Stream.of(firstName, lastName)
	    		.filter(it -> it != null && !it.isBlank())
	    		.collect(Collectors.joining(" "));
	}
}
