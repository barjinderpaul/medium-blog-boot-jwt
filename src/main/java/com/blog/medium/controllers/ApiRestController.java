package com.blog.medium.controllers;

import com.blog.medium.exceptions.InvalidArgumentException;
import com.blog.medium.exceptions.NotFoundException;
import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.repository.PostRepository;
import com.blog.medium.service.CategoryService;
import com.blog.medium.service.PostService;
import com.blog.medium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@org.springframework.web.bind.annotation.RestController
public class ApiRestController {
    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/api/posts/{id}",method = RequestMethod.GET, produces = {"application/json"})
    public Post getPost(@PathVariable("id") String id) {
        Post post = postService.getPost(id);
        return post;
    }

    @RequestMapping(value = "/api/posts" ,method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<Post> filterPostsByCreationDate(@RequestParam(value = "operation",required = false,defaultValue = "and") String operation,
                                                    @RequestParam(value = "user",required = false,defaultValue = "noUser") String username,
                                                    @RequestParam(value = "search",required = false,defaultValue = "") String searchQuery,
                                                    @RequestParam(value = "tag", required = false, defaultValue = "noTag") String tagName,
                                                    @RequestParam(value = "orderBy", required = false, defaultValue = "CreateDateTime") String orderBy,
                                                    @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction,
                                                    @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                                    @RequestParam(value = "size",required = false ,defaultValue = "10") String size) {
        Page<Post> data = null;
        if(searchQuery.equals("")){
            data = postService.filterPostsMethodWithoutSearch(username, tagName, orderBy, direction,operation,page, size);
        }
        else {
            data = postService.filterPostsMethodBySearch(username, tagName, orderBy, direction, operation, searchQuery, page, size);
        }

        if(data.getContent().size() == 0){
            throw new NotFoundException("No posts found for specifc query");
        }
        return data.getContent();
    }

    @RequestMapping(value = "/api/posts",method = RequestMethod.POST)
    public Post addPost(@RequestParam(value = "title", required = false) String title, @RequestParam(value = "content" , required = false) String content, @RequestParam(value = "categories" , required = false) String[] categories) {

        if(title == null){
            throw new InvalidArgumentException("title cannot be null");
        }
        if(content == null) {
            throw new InvalidArgumentException("content cannot be null");
        }

        List<String> categoriesList;
        if(categories != null) {
            categoriesList = Arrays.asList(categories);
        }
        else{
            categoriesList = new ArrayList<>();
            categoriesList.add("uncategorized");
        }
        Long post_id = postService.addPost(title,content,categoriesList);
        Post post = postService.getPost(post_id.toString());
        return post;

    }

    @RequestMapping(value = "/api/posts/{id}",method = RequestMethod.DELETE)
    public void deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
    }

    @RequestMapping(value = "/api/posts/{id}",method = RequestMethod.PUT)
    public Post updatePost(@PathVariable("id") String id, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "content", required = false) String content, @RequestParam(value = "categories", required = false) String[] categories) {

        if(title == null){
            throw new InvalidArgumentException("title cannot be null");
        }
        if(content == null) {
            throw new InvalidArgumentException("content cannot be null");
        }

        List<String> categoriesList;
        if(categories != null) {
            categoriesList = Arrays.asList(categories);
        }
        else{
            categoriesList = new ArrayList<>();
        }
        Long post_id = postService.updatePost(id,title,content,categoriesList);
        Post post = postService.getPost(post_id.toString());
        return post;
    }

    @PatchMapping("/api/posts/{id}")
    public Post updatePostPatch(@PathVariable("id") String id , @RequestParam(value = "title", required = false, defaultValue = "") String title, @RequestParam(value = "content", required = false, defaultValue = "") String content, @RequestParam(value = "categories" , required = false) String[] categories){

        System.out.println("title, content in repository = " + title + "  " + content);
        Long post_id = postService.updatePostPatch(id,title,content,categories);
        return postService.getPost(post_id.toString());

    }

    @GetMapping("/api/posts/category")
    public List<Category> getCategories(){
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @PostMapping("/api/posts/category")
    public Category addCategory(@RequestParam(value = "category", required = false) String categoryName){
        Long category_id = categoryService.addCategory(categoryName);
        return categoryService.getCategory(category_id);
    }



}
