// package: com.product.server.hsf_301.blindBox.service.impl

package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.Blog;
import com.product.server.hsf_301.blindBox.repository.BlogRepository;
import com.product.server.hsf_301.blindBox.repository.UserRepository;
import com.product.server.hsf_301.blindBox.service.BlogService;
import com.product.server.hsf_301.blindBox.service.UserService;
import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserService userService;

    public Page<Blog> getAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Lấy trang đầu, mỗi trang gồm 10 bản ghi
        return blogRepository.findByStatus("PUBLISHED",pageable);
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Blog createBlog(Blog blog) {

        blog.setCreatedAt(LocalDateTime.now());

        blog.setAuthor(userService.getCurrentUser());
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog updatedBlog) {
        return blogRepository.findById(id).map(blog -> {
            blog.setTitle(updatedBlog.getTitle());
            blog.setAuthor(blog.getAuthor());
            blog.setContent(updatedBlog.getContent());
            blog.setStatus(updatedBlog.getStatus());
            return blogRepository.save(blog);
        }).orElseThrow(() -> new RuntimeException("Blog not found with id " + id));
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> getBlogsByAuthor(AppUser currentUserId) {
        return blogRepository.findByAuthor(currentUserId);
    }

    @Override
    public Long getPostCountByAuthor(AppUser currentUserId) {
        return blogRepository.countByAuthor(currentUserId);
    }

    @Override
    public Long getTotalPostCount() {
        return blogRepository.countByAuthor_UserId(2);
    }
}
