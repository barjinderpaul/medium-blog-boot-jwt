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
@Table(name = "categories")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", updatable = false, nullable = false)
    private Long category_id;

    @Column(name = "name")
    private String categoryName;

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", categoryName='" + categoryName + '\'' +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", posts=" + posts +
                '}';
    }

    /*    @Temporal(TemporalType.TIMESTAMP)
        public Date createdAt = new Date();

        @Temporal(TemporalType.TIMESTAMP)
        public Date updatedAt = new Date();*/
    @CreatedDate
    @Column(updatable = false)
    private Date createDateTime;

    @LastModifiedDate
    private Date updateDateTime;


    @ManyToMany(fetch = FetchType.LAZY ,mappedBy = "categories")
    @OrderBy("post_id")
    @JsonIgnore
    private Set<Post> posts = new LinkedHashSet<>();

    public Set<Post> getPosts() {
        return posts;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
