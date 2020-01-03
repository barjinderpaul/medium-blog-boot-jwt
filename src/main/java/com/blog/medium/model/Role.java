package com.blog.medium.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import com.blog.medium.model.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", updatable = true, nullable = false)
    public Long role_id;

    @Column(name = "role_name")
    public String roleName;

/*
    @ManyToMany(fetch = FetchType.LAZY ,mappedBy = "roles")
    @JsonIgnore
    private Set<Post> users = new LinkedHashSet<>();*/

    @ManyToMany(fetch = FetchType.EAGER ,mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new LinkedHashSet<>();


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;

    }
}
