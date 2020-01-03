package com.blog.medium.service;

import com.blog.medium.model.Post;
import com.blog.medium.model.Role;
import com.blog.medium.model.User;
import com.blog.medium.repository.RoleRepository;
import com.blog.medium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Long saveUser(String username, String password, String email) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        Role userRole = roleRepository.findByRoleName("USER");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);

        Long userId = userRepository.save(user).getId();
        return userId;
    }

    @Override
    public List<User> getAllAdminUsers() {
        List<User> allAdminUsers = userRepository.findAllByRoles_roleName("ADMIN");
        return allAdminUsers;
    }

    @Override
    public Long saveAdminUser(String username, String password, String email) {

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        Role userRole = roleRepository.findByRoleName("ADMIN");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);

        Long userId = userRepository.save(user).getId();
        return userId;

    }
}
