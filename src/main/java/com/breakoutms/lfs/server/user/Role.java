package com.breakoutms.lfs.server.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class Role implements GrantedAuthority {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
 
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Privilege.class)
    @Column(name = "privilege")
    private List<Privilege> privileges;

	@Override
	public String getAuthority() {
		return name;
	}


}