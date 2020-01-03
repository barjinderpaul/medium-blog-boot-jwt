package com.blog.medium.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = true, nullable = false)
    public Long id;

    @Column(name = "name")
    public String username;

    public String email;
    public String password;

    /*@Temporal(TemporalType.TIMESTAMP)
    public Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    public Date updatedAt = new Date();*/

    @CreatedDate
    @Column(updatable = false)
    private Date createDateTime;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", posts=" + posts +
                '}';
    }

    @LastModifiedDate
    private Date updateDateTime;
    @OneToMany(mappedBy="user")
    @OrderBy("post_id")
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userRole",
            joinColumns = { @JoinColumn(name = "user_id", unique = false) },
            inverseJoinColumns = { @JoinColumn(name = "role_id", unique = false) })
    private Set<Role> roles = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public LocalDateTime getCreatedAt() {
        return createDateTime;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createDateTime = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updateDateTime;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updateDateTime = updatedAt;
    }*/

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
