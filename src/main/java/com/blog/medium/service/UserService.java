package com.blog.medium.service;

import com.blog.medium.model.Post;
import com.blog.medium.model.User;

import java.util.List;

public interface UserService {
//    List<Post> getUserPosts(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();

    Long saveUser(String username, String password, String email);

    List<User> getAllAdminUsers();

    Long saveAdminUser(String username, String password, String email);
}
