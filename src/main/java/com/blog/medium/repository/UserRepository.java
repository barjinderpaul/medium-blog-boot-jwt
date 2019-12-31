package com.blog.medium.repository;

import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
/*    User findUserByUsername(String userName);
    Page findPostsByUsernameAndPosts_Categories_categoryNameContains(String username, String category, Pageable pageable);*/
}
