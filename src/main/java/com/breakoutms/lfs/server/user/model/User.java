package com.breakoutms.lfs.server.user.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

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

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles", 
            joinColumns = @JoinColumn(
              name = "user_id", referencedColumnName = "id"), 
            inverseJoinColumns = @JoinColumn(
              name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

	public String getFullnames() {
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(firstName)){
			sb.append(firstName);
		}
		if(StringUtils.isNotBlank(lastName)) {
			sb.append(" ").append(lastName);
		}
		return sb.toString().trim();
	}
}
