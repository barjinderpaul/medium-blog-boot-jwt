package com.blog.medium.repository;

import com.blog.medium.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Repository
public interface PostRepository<P> extends JpaRepository<Post,Long> {
    List<P>  findAllByOrderByIdAsc();
    Page<P> findAllByOrderByIdAsc(Pageable pageable);
    List<P> findAllByOrderByUpdateDateTimeDesc();
    List<P> findAllByOrderByPublishedAtDesc();

    Page<Post> findAllByCategories_categoryNameLike(String category, Pageable pageable);

    Page<Post> findAllByUser_username(String username, Pageable pageable);

    Page<Post> findDistinctByTitleContainsOrContentContainsOrCategories_categoryNameLike(String word, String word2, String categoryName,Pageable pageable);

    Page<Post> findDistinctByCategories_categoryNameIn(String[] categories, Pageable pageable);

    Page findAllByUser_usernameAndCategories_categoryNameContains(String username, String category, Pageable pageable);

    Page findByUser_usernameAndCategories_categoryNameIn(String username, String[] categories, Pageable pageable);

    Page<Post> findDistinctByUser_usernameAndTitleContainingOrUser_usernameAndContentContainingOrUser_usernameAndCategories_categoryNameContains(String username,String titleWord,String username2, String contentWord,String username3 ,String categoryName, Pageable pageable);

    Page<Post> findDistinctByUser_usernameAndCategories_categoryNameAndTitleContainingOrUser_usernameAndCategories_categoryNameAndContentContainingOrUser_usernameAndCategories_categoryNameAndCategories_categoryName(String username, String categoryName, String title, String username2, String categoryName2, String content, String username3, String categoryName3, String categoryToSearch,Pageable pageable
    );

    Page<Post> findDistinctByCategories_categoryNameInAndTitleContainsOrCategories_categoryNameInAndContentContains(String[] categoroes, String title, String []categories2, String content, Pageable pageable);


    Page<Post> findDistinctByCategories_categoryNameLikeAndTitleContainsOrCategories_categoryNameLikeAndContentContains(String tagName, String query, String tagNameRepeat, String queryRepeat, Pageable pageable);

}
