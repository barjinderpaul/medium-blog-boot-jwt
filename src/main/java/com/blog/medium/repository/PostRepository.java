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
    Page<Post> findByCategories(String name, Pageable pageable);

    /*findAllBy single tag*/
    Page<Post> findAllByCategories_categoryNameLike(String category, Pageable pageable);

    /*findALl posts by user*/
    Page<Post> findAllByUser_username(String username, Pageable pageable);

    /*FilterRepository*/
    Page<Post> findDistinctByTitleContainsOrContentContainsOrCategories_categoryNameLike(String word, String word2, String categoryName,Pageable pageable);

    /*Testing category*/
    Page<Post> findDistinctByCategories_categoryNameIn(String[] categories, Pageable pageable);

    /*Username with single tag*/
    Page findAllByUser_usernameAndCategories_categoryNameContains(String username, String category, Pageable pageable);

    /*Username with multiple tags*/
    Page findByUser_usernameAndCategories_categoryNameIn(String username, String[] categories, Pageable pageable);

   /* *//*Search with user provided*//*
    Page<Post> findDistinctByTitleContainingOrContentContainingOrCategories_categoryNameContainsAndUser_username(String titleWord, String contentWord, String categoryName, String username, Pageable pageable);
*/
    Page<Post> findDistinctByUser_usernameAndTitleContainingOrUser_usernameAndContentContainingOrUser_usernameAndCategories_categoryNameContains(String username,String titleWord,String username2, String contentWord,String username3 ,String categoryName, Pageable pageable);

    /*Search with user and category provided*/
    /*
    *
    *   Basic property :
    *   A /\ (B \/ C) <=> (A /\ B) \/ (A /\ C)
    *   A and (B or C) <=> (A and B) or (A and C)
    *
    *   Custom Property :
    *   A /\ B /\ (C \/ D \/ E) <==> (A /\ B /\ C ) \/ (A /\ B /\ D ) \/ (A /\ B /\ C)
    *   A and B and (C or D or E ) <==> (A and B and C) or (A and B and D) or (A and B and E)
    *   Username and Category and (title or content or category)
    * */
    Page<Post> findDistinctByUser_usernameAndCategories_categoryNameAndTitleContainingOrUser_usernameAndCategories_categoryNameAndContentContainingOrUser_usernameAndCategories_categoryNameAndCategories_categoryName(String username, String categoryName, String title, String username2, String categoryName2, String content, String username3, String categoryName3, String categoryToSearch,Pageable pageable
    );

  /*  *//*Search with multiple categories with user provided*//*
    Page<Post> findByUser_usernameAndCategories_categoryNameInAndTitleContainingOrUser_usernameAndCategories_categoryNameInAndContentContainingOrUser_usernameAndCategories_categoryNameInAndCategories_categoryName(String username, String[] categoryNames, String title, String username2, String categoryNames2[], String content, String username3, String categoryNames3[], String categoryToSearch,Pageable pageable
    );*/

    /*Search with multiple tags */
    Page<Post> findDistinctByCategories_categoryNameInAndTitleContainsOrCategories_categoryNameInAndContentContains(String[] categoroes, String title, String []categories2, String content, Pageable pageable);

    /*Search with single tag*/
    Page<Post> findDistinctByCategories_categoryNameLikeAndTitleContainsOrCategories_categoryNameLikeAndContentContains(String tagName, String query, String tagNameRepeat, String queryRepeat, Pageable pageable);

}
