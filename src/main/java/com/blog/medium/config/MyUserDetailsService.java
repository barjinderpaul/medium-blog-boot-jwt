package com.blog.medium.config;

import com.blog.medium.model.User;
import com.blog.medium.repository.UserRepository;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.*;
import java.util.ArrayList;

@Service
public class MyUserDetailsService  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        CustomUserDetails customUserDetails = null;
        if(user != null) {
            customUserDetails = new CustomUserDetails();
            customUserDetails.setUser(user);
        }
        else {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        return customUserDetails;
    }
}
