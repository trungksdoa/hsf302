// package: com.product.server.hsf_301.blindBox.service

package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.Blog;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    List<Blog> getAllBlogs();
    Optional<Blog> getBlogById(Long id);
    Blog createBlog(Blog blog);
    Blog updateBlog(Long id, Blog blog);
    void deleteBlog(Long id);
}
