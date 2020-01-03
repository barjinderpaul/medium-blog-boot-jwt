/*
package com.blog.medium.controllers;

import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import com.blog.medium.repository.PostRepository;
import com.blog.medium.repository.UserRepository;
import com.blog.medium.service.CategoryService;
import com.blog.medium.service.PostService;
import com.blog.medium.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Controller
//@RestController
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/",method = RequestMethod.GET, produces = {"application/json"})
    public ModelAndView filterPostsHome(
            @RequestParam(value = "page",required = false, defaultValue = "0") String page,
            @RequestParam(value = "size",required = false ,defaultValue = "2") String size,
            @RequestParam(value = "orderBy", required = false, defaultValue = "CreateDateTime") String orderBy,
            @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction)
    {

        Page data =  postService.getAllPostsHome(page,size,orderBy,direction);
*/
/*        List<Category> categories = categoryService.getAllTags();
        List<User> users = userService.getAllUsers(page,size,orderBy,direction);*//*


        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("blogPosts");
        modelAndView.addObject("allPosts",data.getContent());
        modelAndView.addObject("postsPage",data);*/
/*
        modelAndView.addObject("allCategories",categories);
        modelAndView.addObject("allUsers",users);*//*

        modelAndView.addObject("numbers", IntStream.range(0,data.getTotalPages()).toArray());
        return modelAndView;

//        return data.getContent();
    }

    @RequestMapping(value = "posts/{id}",method = RequestMethod.GET)
    public ModelAndView getPost(@PathVariable("id") String id) {

        Post post = postService.getPost(id);
        Set<Category> categorySet = post.getCategories();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("singlePost");
        modelAndView.addObject("post",post);
        modelAndView.addObject("categorySet",categorySet);

        return modelAndView;
    }

    @RequestMapping(value = "/posts" ,method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView filterPostsByCreationDate(@RequestParam(value = "operation",required = false,defaultValue = "and") String operation,
                                                  @RequestParam(value = "user",required = false,defaultValue = "noUser") String username,
                                                  @RequestParam(value = "search",required = false,defaultValue = "") String searchQuery,
                                                  @RequestParam(value = "tag", required = false, defaultValue = "noTag") String tagName,
                                                  @RequestParam(value = "orderBy", required = false, defaultValue = "CreateDateTime") String orderBy,
                                                  @RequestParam(value = "direction",required = false, defaultValue = "DESC") String direction,
                                                  @RequestParam(value = "page",required = false, defaultValue = "0") String page,
                                                  @RequestParam(value = "size",required = false ,defaultValue = "2") String size) {

        Page<Post> data = null;
        if(searchQuery.equals("")){
             data = postService.filterPostsMethodWithoutSearch(username, tagName, orderBy, direction,operation,page, size);
        }
        else {
            data = postService.filterPostsMethodBySearch(username, tagName, orderBy, direction, operation, searchQuery, page, size);
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("filteredPosts");
        modelAndView.addObject("posts",data.getContent());
        modelAndView.addObject("postsPage",data);
        modelAndView.addObject("numbers", IntStream.range(0,data.getTotalPages()).toArray());
        return modelAndView;
    }

    @RequestMapping(value = "posts/add",method = RequestMethod.GET)
    public ModelAndView redirectToCreatePost(){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("createPost");
        modelAndView.addObject("heading","Add Post");
        modelAndView.addObject("customAction","addPost");

        return modelAndView;
    }

    @RequestMapping(value = "posts/add",method = RequestMethod.POST)
    public ModelAndView addPost(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "categories" , required = false) String[] categories) {

        List<String> categoriesList;
        if(categories != null) {
            categoriesList = Arrays.asList(categories);
        }
        else{
            categoriesList = new ArrayList<>();
            categoriesList.add("uncategorized");
        }
        Long post_id = postService.addPost(title,content,categoriesList);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("createPost");
        modelAndView.addObject("postCreated","true");
        modelAndView.addObject("title",title);
        modelAndView.addObject("heading","Post created");
        modelAndView.addObject("id",post_id);
        modelAndView.addObject("customAction","postCreated");

        return modelAndView;
    }




    @RequestMapping(value = "posts/delete/{id}", method = RequestMethod.GET)
    public ModelAndView redirectToDeletePage(@PathVariable("id") String id){

        Post post = postService.getPost(id);

        String content = post.getContent();
        String title = post.getTitle();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deletePost");
        modelAndView.addObject("customAction","addPost");
        modelAndView.addObject("id",Long.parseLong(id));
        modelAndView.addObject("content",content);
        modelAndView.addObject("title",title);

        return modelAndView;
    }

    @RequestMapping(value = "posts/delete/{id}",method = RequestMethod.POST)
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/?";

    }

    @RequestMapping(value = "posts/update/{id}" ,method = RequestMethod.GET)
    public ModelAndView redirectToUpdatePost( @PathVariable("id") String id) {

        Post post = postService.getPost(id);
        Set<Category> categorySet = post.getCategories();
        String content = post.getContent();
        String title = post.getTitle();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("createPost");
        modelAndView.addObject("heading","Update Post");
        modelAndView.addObject("customAction","updatePost");
        modelAndView.addObject("content","some-sample-content");
        modelAndView.addObject("id",Long.parseLong(id));
        modelAndView.addObject("content",content);
        modelAndView.addObject("title",title);
        modelAndView.addObject("categorySet",categorySet);

        return modelAndView;
    }

    @RequestMapping(value = "posts/update/{id}",method = RequestMethod.POST)
    public String updatePost(@PathVariable("id") String id, @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "categories", required = false) String[] categories) {

        List<String> categoriesList;
        if(categories != null) {
            categoriesList = Arrays.asList(categories);
        }
        else{
            categoriesList = new ArrayList<>();
        }

        postService.updatePost(id,title,content,categoriesList);
        return "redirect:/posts/{id}";
    }


}
*/
