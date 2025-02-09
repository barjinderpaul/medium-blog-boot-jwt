package com.blog.medium.service;

import com.blog.medium.model.Post;
import com.blog.medium.model.Role;
import com.blog.medium.model.User;
import com.blog.medium.repository.RoleRepository;
import com.blog.medium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

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
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmail(email);

        Role userRole = roleRepository.findByRoleName("USER");
        if(userRole == null ){
            Role userR = new Role();
            userR.setRoleName("USER");
            roleRepository.save(userR);
            userRole = roleRepository.findByRoleName("USER");
        }
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
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmail(email);

        Role userRole = roleRepository.findByRoleName("ADMIN");
        if(userRole == null) {
            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);
            userRole = roleRepository.findByRoleName("ADMIN");
        }
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);
        userRepository.save(user);

        Long userId = userRepository.save(user).getId();
        return userId;

    }
}
