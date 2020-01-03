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
    public List<Category> getAllTags() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public List<Post> findJsonDataByCondition(Long categoryId,String categoryName, String orderBy, String direction, int page, int size) {
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
//        Category category = categoryRepository.findById(categoryId).get();
//        Page<Post> data = category.getPosts();
        Page<Post> data= postRepository.findByCategories(categoryName,pageable);
        System.out.println("PADASDASD DATAAA = " + data.getContent());
        return data.getContent();
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
