package com.breakoutms.lfs.server.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "username", name = "unique_username", unique=true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "SMALLINT UNSIGNED")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "users_roles", 
            joinColumns = @JoinColumn(
              name = "user_id", referencedColumnName = "id"), 
            inverseJoinColumns = @JoinColumn(
              name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;
}
