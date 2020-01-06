package com.blog.medium.service;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    Category getCategory(Long id);
    List<Category> findAll();
    Long addCategory(String categoryName);
}
