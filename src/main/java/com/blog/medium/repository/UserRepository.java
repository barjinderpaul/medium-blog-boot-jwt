package com.blog.medium.repository;

import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    List<User> findAllByRoles_roleName(String role);
}
