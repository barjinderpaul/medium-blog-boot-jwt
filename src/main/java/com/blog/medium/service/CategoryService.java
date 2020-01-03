package com.blog.medium.service;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    List<Category> getAllTags();
    Category getCategory(Long id);
    List<Post> findJsonDataByCondition(Long id, String categoryName,String orderBy, String direction, int page, int size);

    List<Category> findAll();
    Long addCategory(String categoryName);
}
