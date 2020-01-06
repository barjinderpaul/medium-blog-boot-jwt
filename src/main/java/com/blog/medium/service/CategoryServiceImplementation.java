package com.blog.medium.service;

import com.blog.medium.exceptions.InvalidArgumentException;
import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.repository.CategoryRepository;
import com.blog.medium.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Long addCategory(String categoryName) {
        if(categoryName == null || categoryName.equals("")){
            throw  new InvalidArgumentException("Category name cannot null/empty");
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        Long categoryId = categoryRepository.save(category).getCategory_id();
        return categoryId;
    }
}
