package com.breakoutms.lfs.server.user.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.common.PrivilegeType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @Builder
@ToString(exclude="roles")
@AllArgsConstructor @NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "type", name = "unique_privilege_type", unique=true)
})
public class Privilege {
		
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false, columnDefinition = "CHAR(15)")
    @Enumerated(EnumType.STRING)
    private PrivilegeType type;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "privileges")
    private List<Role> roles;
    
    public Privilege(PrivilegeType type) {
    	this.type = type;
    }
}
