package com.blog.medium;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MediumApplication{
    public static void main(String[] args) {
        SpringApplication.run(MediumApplication.class, args);
    }
}
