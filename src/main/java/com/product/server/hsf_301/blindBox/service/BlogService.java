// package: com.product.server.hsf_301.blindBox.service

package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.Blog;
import com.product.server.hsf_301.user.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    Page<Blog> getAllBlogs(int page, int size);
    Optional<Blog> getBlogById(Long id);
    Blog createBlog(Blog blog);
    Blog updateBlog(Long id, Blog blog);
    void deleteBlog(Long id);

    List<Blog> getBlogsByAuthor(AppUser appUser);

    Long getPostCountByAuthor(AppUser currentUserId);

    Long getTotalPostCount();
}
