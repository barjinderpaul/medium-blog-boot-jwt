package com.blog.medium.service;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PostService {
    List<Post> getAllPosts();
    Post getPost(String id);
    Long addPost(String title, String content, List<String> categoriesList);
    void deletePost(Long id);
    Long updatePost(String id, String title, String content, List<String> categoriesList);
    Long updatePostPatch(String id,String title,String content,String [] categories);

    Page<Post> filterPostsMethodWithoutSearch(String username, String tagName, String orderBy, String direction, String operation, String page, String size);

    Page<Post> filterPostsMethodBySearch(String username, String tagName, String orderBy, String direction, String operation, String searchQuery, String page, String size);

    List<Post> getAllPostsSortedByPublishDate();
    List<Post> getAllPostsSortedByLastUpdate();
    Page<Post> getPostsByMultipleTags(String[] tags, String orderBy, String direction, Integer pageNo, Integer pageSize);

    Page<Post> getAllPostsHome(String page, String pageSize, String orderBy, String direction);
}
