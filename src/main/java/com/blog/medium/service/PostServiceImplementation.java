package com.blog.medium.service;
import com.blog.medium.exceptions.InvalidArgumentException;
import com.blog.medium.exceptions.NotFoundException;
import com.blog.medium.exceptions.UnauthorizedAccessException;
import com.blog.medium.model.Category;
import com.blog.medium.model.Post;
import com.blog.medium.model.User;
import com.blog.medium.repository.CategoryRepository;
import com.blog.medium.repository.PostRepository;

import com.blog.medium.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.*;


@Service("PostService")
@Transactional
@Slf4j
public class PostServiceImplementation implements PostService {

    @Autowired
    private PostRepository<Post> postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByIdAsc();
    }

    public List<Post> getAllPostsSortedByPublishDate() {
        return postRepository.findAllByOrderByPublishedAtDesc();
    }

    public List<Post> getAllPostsSortedByLastUpdate() {
        return postRepository.findAllByOrderByUpdateDateTimeDesc();
    }

    /*
    * Exception checks
    * */

    private void checkValidId(String id){
        Long post_id;
        try {
            post_id = Long.parseLong(id);
        }
        catch (NumberFormatException e){
            throw new InvalidArgumentException("Please, enter a valid id");
        }
    }

    private void checkPostAndUser(Post post, String username){
        if( !(post.getUser().getUsername().equals(username)) && !(userRepository.findByUsername(username).getRoles().toString().contains("ADMIN"))) {
            throw new UnauthorizedAccessException("Username : " + username + " has no right access to this post");
        }
    }

    private void checkValidCategory(Category category) {
        if(category == null){
            throw new NotFoundException("Please, enter a valid category. No category: " + category + " exists");
        }
    }

    private void checkNullAndValidArguments(String username, String category, String orderBy, String direction, String operation, String page, String size){

        if( !(username.toLowerCase().equals("nouser")) && (userRepository.findByUsername(username.toLowerCase()) == null)){
            throw new NotFoundException("User: '" + username + "' does not exist");
        }
        else if(category.contains(",")){
            String[] categories = category.split(",");
            for(String categoryName: categories){
                Category categoryDB;
                try {
                    categoryDB = categoryRepository.findByCategoryName(categoryName);
                }
                catch (NotFoundException e) {
                    throw new InvalidArgumentException("Category: " + categoryName + " does not exists");
                }
            }
        }
        else if(!(category.equals("noTag"))){
            if(category == null) {
                throw new InvalidArgumentException("No category: " + category +" exists");
            }
            else{
                Category categoryDB = categoryRepository.findByCategoryName(category);
                if(categoryDB == null){
                    throw new InvalidArgumentException("No category: "+ category +" exists");
                }
            }
        }

        if(Integer.parseInt(size)<1){
            throw new InvalidArgumentException("Posts fetch size cannot be less than 0");
        }

        if(Integer.parseInt(size) > 25){
            throw new InvalidArgumentException("Posts fetch size should be less than 25");
        }
        if(( !(direction.equals("ASC")) && (!(direction.equals("DESC"))) )) {
            throw new InvalidArgumentException("Direction/Sort By: " + direction + " is not valid");
        }
        if((!(orderBy.equals("CreateDateTime")) && !(orderBy.equals("UpdateDateTime")))){
            throw new InvalidArgumentException("Order By: " + orderBy + " is not valid");
        }
        if((!(operation.equals("and")) && !(operation.equals("or")))){
            throw new InvalidArgumentException("operation: " + operation + " not valid");
        }

    }


    /* CRUD Operations */

    public Post getPost(String id) {

        checkValidId(id);
        Long postId = Long.parseLong(id);

        Optional<Post> post = postRepository.findById(postId);
        if( !(post.isPresent() ) ){
            throw new NotFoundException("GET: No post found with id + " + id);
        }
        return post.get();
    }

    public Long addPost(String title, String content, List<String> categories, String username) {
    /*
    No user exists:
    User user = new User(); user.setEmail("admin@admin.com");user.setUsername("admin");user.setPassword("admin");
    */
        User userDB = userRepository.findByUsername(username);
        if(userDB == null) {
            throw new InvalidArgumentException("User: " + username + " does not exists");
        }
        Optional<User> userOptional = userRepository.findById(userDB.getId());
/*
        if(!(userOptional.isPresent())){
            throw new NotFoundException("User with id = " + id + " not found");
        }
*/
        User user = userOptional.get();
        Post post = new Post();
        post.setPublishedAt(LocalDateTime.now());
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);

        System.out.println("Categories = " + categories.toString());

        for (String category : categories) {
            Category categoryFound = categoryRepository.findByCategoryName(category);
            if(categoryFound == null){
                Category newCategory = new Category();
                newCategory.setCategoryName(category);
                categoryFound = newCategory;
            }
            checkValidCategory(categoryFound);
            categoryFound.getPosts().add(post);
            post.getCategories().add(categoryFound);
        }



        user.getPosts().add(post);
        userRepository.save(user);
        Long id = postRepository.save(post).getId();
        return id;
    }

    public void deletePost(Long id, String username) {

        Optional<Post> optionalPost = postRepository.findById(id);

        if(!(optionalPost.isPresent() ) ){
            throw new NotFoundException("DELETE : No post found with id + " + id);
        }

        Post post = optionalPost.get();

        checkPostAndUser(post,username);

        postRepository.deleteById(id);
    }

    public Long updatePost(String id, String title, String content, List<String> categoriesList, String username) {
        checkValidId(id);
        Long postId = Long.parseLong(id);

        Optional<Post> optionalPost = postRepository.findById(postId);
        if(!(optionalPost.isPresent())){
                throw new NotFoundException("PUT : No post found with id + " + postId);
        }
        Post postFromDB = optionalPost.get();

        checkPostAndUser(postFromDB,username);

        postFromDB.setContent(content);
        postFromDB.setTitle(title);

        for (String categoryName : categoriesList) {
            Category category = categoryRepository.findByCategoryName(categoryName);
            if(category == null) {
                Category newCategory = new Category();
                newCategory.setCategoryName(categoryName);
                category = newCategory;
            }
            checkValidCategory(category);
            if (!(postFromDB.getCategories().contains(category))) {
                postFromDB.getCategories().add(category);
                category.getPosts().add(postFromDB);
            }
        }

        if (postFromDB.getCategories().size() > 1) {
            Category uncategorizedCategory = categoryRepository.findByCategoryName("uncategorized");
            if (postFromDB.getCategories().contains(uncategorizedCategory)) {
                postFromDB.getCategories().remove(uncategorizedCategory);
                uncategorizedCategory.getPosts().remove(postFromDB);
            }
        }
        Long post_id = postRepository.save(postFromDB).getId();
        return post_id;
    }

    @Override
    public Long updatePostPatch(String id, String title, String content, String[] categories, String username) {

        checkValidId(id);
        Long postId = Long.parseLong(id);

        Optional postOptional = postRepository.findById(postId);
        if(!(postOptional.isPresent())){
            throw new NotFoundException("PATCH : No post found with id + " + postId);
        }
        Post post = (Post) postOptional.get();

        System.out.println("USERNAME, USERNAME POST :" + username + " " + post.getUser().getUsername());

        checkPostAndUser(post,username);

        if(!(title.equals(""))){
            System.out.println("Title here");
            post.setTitle(title);
        }
        if(!(content.equals(""))){
            System.out.println("Content here");
            post.setContent(content);
        }
        Set<Category> categoriesPresent = post.getCategories();
        if(categories!= null && categories.length > 0) {
            for (String categoryName : categories) {
                Category category = categoryRepository.findByCategoryName(categoryName);
                checkValidCategory(category);
                if (!(categoriesPresent.contains(category))) {
                    categoriesPresent.add(category);
                    category.getPosts().add(post);
                }
            }
        }
        Long post_id = postRepository.save(post).getId();
        return post_id;
    }

    /* Filter operations */

    private Pageable getPageable(String orderBy, String direction, Integer pageNo, Integer pageSize) {
        System.out.println("orderBy , direction, pageNo, pageSize = " + orderBy + " " + direction + " " + pageNo + " " + pageSize);
        Sort sort = null;
        if (direction.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        }
        if (direction.equals("DESC")) {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return pageable;
    }

    private Page getCustomPage(Integer pageNo, Integer pageSize, List<Post> data) {
        long start = PageRequest.of(pageNo, pageSize).getOffset();
        long end = (start + PageRequest.of(pageNo, pageSize).getPageSize()) > data.size() ? data.size() : (start + PageRequest.of(pageNo, pageSize).getPageSize());
        return new PageImpl<Post>(data.subList((int) start, (int) end), PageRequest.of(pageNo, pageSize), data.size());
    }

    /*
     * filter posts by search methods:
     * */

    private Page getPostsByUsernameAndSearchAndMultipleCategoriesAndOperation(String username, String tagName, String searchQuery, String orderBy, String direction, Integer pageNo, Integer pageSize) {
        String[] categories = tagName.split(",");

        List<Post> allPosts = postRepository.findAll();
        List<Category> categoryList = new ArrayList<>();
        for (String categoryName : categories) {
            Category category = categoryRepository.findByCategoryName(categoryName);
            checkValidCategory(category);
            categoryList.add(category);
        }

        List<Post> allPostsWithAllCategories = new ArrayList<>();
        for (Post post : allPosts) {
            if (post.getCategories().containsAll(categoryList) && post.getUser().getUsername().toLowerCase().equals(username) && (post.getTitle().contains(searchQuery) || post.getContent().contains(searchQuery))) {
                System.out.println("MATCH FOUND");
                allPostsWithAllCategories.add(post);
            }
        }
        return getCustomPage(pageNo, pageSize, allPostsWithAllCategories);
    }

    private Page getPostshWithSearchAndUserAndCategory(String username, String categoryName, String searchQuery, Pageable pageable) {
        return postRepository.findDistinctByUser_usernameAndCategories_categoryNameAndTitleContainingOrUser_usernameAndCategories_categoryNameAndContentContainingOrUser_usernameAndCategories_categoryNameAndCategories_categoryName(username, categoryName, searchQuery, username, categoryName, searchQuery, username, categoryName, searchQuery, pageable);
    }

    private Page getPostsWithSearchAndUsername(String username, String searchQuery, Pageable pageable) {
        return postRepository.findDistinctByUser_usernameAndTitleContainingOrUser_usernameAndContentContainingOrUser_usernameAndCategories_categoryNameContains(username, searchQuery, username, searchQuery, username, searchQuery, pageable);
    }

    private Page getPostsWithSearchAndMultipleTags(String[] categories, String searchQuery, Pageable pageable) {
        return postRepository.findDistinctByCategories_categoryNameInAndTitleContainsOrCategories_categoryNameInAndContentContains(categories, searchQuery, categories, searchQuery, pageable);
    }

    private Page getPostsWithSearchAndCategory(String categoryName, String searchQuery, Pageable pageable) {
        return postRepository.findDistinctByCategories_categoryNameLikeAndTitleContainsOrCategories_categoryNameLikeAndContentContains(categoryName, searchQuery, categoryName, searchQuery, pageable);
    }

    private Page<Post> getPostsWithSearch(String searchQuery, String orderBy, String direction, Integer pageNo, Integer pageSize, Pageable pageable) {
        return postRepository.findDistinctByTitleContainsOrContentContainsOrCategories_categoryNameLike(searchQuery, searchQuery, searchQuery, pageable);
    }

    public Page<Post> filterPostsMethodBySearch(String username, String tagName, String orderBy, String direction, String operation, String searchQuery, String page, String size) {
        Integer pageNo = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);
        checkNullAndValidArguments(username, tagName, orderBy, direction, operation, page, size);
        Pageable pageable = getPageable(orderBy, direction, pageNo, pageSize);
        String[] categories = tagName.split(",");

        if(searchQuery==null){
            throw new NotFoundException("Search query should not be null");
        }
//        checkNullAndValidArguments(username, tagName, orderBy, direction, operation, page, size);

        Page data = null;
        if (tagName.contains(",") && !(username.toLowerCase().equals("nouser"))) {
            data = getPostsByUsernameAndSearchAndMultipleCategoriesAndOperation(username, tagName, searchQuery, orderBy, direction, pageNo, pageSize);
        } else if ((!(username.toLowerCase().equals("nouser"))) && tagName != null && !(tagName.toLowerCase().equals("notag"))) {
            data = getPostshWithSearchAndUserAndCategory(username, tagName, searchQuery, pageable);
        } else if ((!(username.toLowerCase().equals("nouser")))) {
            data = getPostsWithSearchAndUsername(username, searchQuery, pageable);
        } else if (tagName.contains(",")) {
            data = getPostsWithSearchAndMultipleTags(categories, searchQuery, pageable);
        } else if (tagName != null && !(tagName.toLowerCase().equals("notag"))) {
            data = getPostsWithSearchAndCategory(tagName, searchQuery, pageable);
        } else {
            data = getPostsWithSearch(searchQuery, orderBy, direction, pageNo, pageSize, pageable);
        }
        return data;
    }

    /*
     * filter posts without search methods
     * */

    private Page<Post> getPostsWithUserAndMultipleCategoriesOrOperation(String username, String[] categories, Pageable pageable) {
        return postRepository.findByUser_usernameAndCategories_categoryNameIn(username, categories, pageable);
    }


    private Page<Post> getPostsWithUserAndCategory(String username, String tagName, Pageable pageable) {
        return postRepository.findAllByUser_usernameAndCategories_categoryNameContains(username, tagName, pageable);
    }

    private Page<Post> getPostsWithUserAndMultipleCategoriesAndOperation(String username, String[] categories, Integer pageNo, Integer pageSize) {
        List<Post> allPosts = postRepository.findAll();
        List<Category> categoryList = new ArrayList<>();
        for (String categoryName : categories) {
            Category category = categoryRepository.findByCategoryName(categoryName);
            categoryList.add(category);
        }

        List<Post> allPostsWithAllCategories = new ArrayList<>();
        for (Post post : allPosts) {
            if (post.getCategories().containsAll(categoryList) && post.getUser().getUsername().toLowerCase().equals(username)) {
                allPostsWithAllCategories.add(post);
            }
        }

        return getCustomPage(pageNo, pageSize, allPostsWithAllCategories);
    }


    @Override
    public Page<Post> getPostsByMultipleTags(String[] categories, String orderBy, String direction, Integer pageNo, Integer pageSize) {

        List<Post> allPosts = postRepository.findAll();
        List<Category> categoryList = new ArrayList<>();
        for (String categoryName : categories) {
            Category category = categoryRepository.findByCategoryName(categoryName);
            categoryList.add(category);
        }

        List<Post> allPostsWithAllCategories = new ArrayList<>();
        for (Post post : allPosts) {
            if (post.getCategories().containsAll(categoryList)) {
                allPostsWithAllCategories.add(post);
            }
        }

        return getCustomPage(pageNo, pageSize, allPostsWithAllCategories);
    }

    @Override
    public Page<Post> getAllPostsHome(String page, String pageLength, String orderBy, String direction){
        Integer pageNo = Integer.parseInt(page);
        Integer pageSiae = Integer.parseInt(pageLength);
        Pageable pageable = getPageable(orderBy, direction, pageNo, pageSiae);
        return postRepository.findAllByOrderByIdAsc(pageable);
    }

    private Page<Post> getPostsByMultipleTagsOrOperation(String[] categories, Pageable pageable) {
        return postRepository.findDistinctByCategories_categoryNameIn(categories, pageable);
    }

    private Page<Post> getPostsByUser(String username, Pageable pageable) {
        return postRepository.findAllByUser_username(username, pageable);
    }

    private Page<Post> getPostsByCategory(String category, Pageable pageable) {
        return postRepository.findAllByCategories_categoryNameLike(category, pageable);
    }

    private Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByIdAsc(pageable);
    }

    public Page<Post> filterPostsMethodWithoutSearch(String username, String tagName, String orderBy, String direction, String operation, String page, String size) {
        Integer pageNo = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);
        checkNullAndValidArguments(username, tagName, orderBy, direction, operation, page, size);
        Pageable pageable = getPageable(orderBy, direction, pageNo, pageSize);
        String[] categories = tagName.split(",");



        Page data = null;

        if (tagName.contains(",") && (!(username.toLowerCase().equals("nouser")))) {
            if (operation.toLowerCase().equals("or")) {
                data = getPostsWithUserAndMultipleCategoriesOrOperation(username, categories, pageable);
            } else {
                data = getPostsWithUserAndMultipleCategoriesAndOperation(username, categories, pageNo, pageSize);
            }
        }
        else if (tagName != null && !(tagName.toLowerCase().equals("notag")) && !(username.equals("noUser"))) {
            data = getPostsWithUserAndCategory(username, tagName, pageable);
        }
        else if (tagName.contains(",")) {
            if (operation.toLowerCase().equals("and")) {
                data = getPostsByMultipleTags(categories, orderBy, direction, pageNo, pageSize);
            }
            else if (operation.toLowerCase().equals("or")) {
                data = getPostsByMultipleTagsOrOperation(categories, pageable);
            }
        }
        else if (!(username.equals("noUser"))) {
            data = getPostsByUser(username, pageable);
        }
        else if (tagName != null && !(tagName.toLowerCase().equals("notag"))) {
            data = getPostsByCategory(tagName, pageable);
        }
        else {
            data = getAllPosts(pageable);
        }
        return data;
    }
}
