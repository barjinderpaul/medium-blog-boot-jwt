package com.blog.medium.service;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    Post getPost(String id);
    Long addPost(String title, String content, List<String> categoriesList, String username);
    void deletePost(String id, String username);
    Long updatePost(String id, String title, String content, List<String> categoriesList, String username);
    Long updatePostPatch(String id,String title,String content,String [] categories, String username);

    Page<Post> filterPostsMethodWithoutSearch(String username, String tagName, String orderBy, String direction, String operation, String page, String size);

    Page<Post> filterPostsBySearch(String username, String tagName, String orderBy, String direction, String operation, String searchQuery, String page, String size);


}
