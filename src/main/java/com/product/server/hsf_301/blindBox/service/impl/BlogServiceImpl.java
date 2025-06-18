// package: com.product.server.hsf_301.blindBox.service.impl

package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.Blog;
import com.product.server.hsf_301.blindBox.repository.BlogRepository;
import com.product.server.hsf_301.blindBox.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public  class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;




    public Page<Blog> getAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Lấy trang đầu, mỗi trang gồm 10 bản ghi
        return blogRepository.findAll(pageable);
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Blog createBlog(Blog blog) {
        blog.setCreatedAt(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog updatedBlog) {
        return blogRepository.findById(id).map(blog -> {
            blog.setTitle(updatedBlog.getTitle());
            blog.setAuthor(updatedBlog.getAuthor());
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
    public List<Blog> getBlogsByAuthor(String currentUserId){
        return blogRepository.findAllByAuthor_UserId(Integer.parseInt(currentUserId));
    }

    @Override
    public Blog getPostCountByAuthor(String currentUserId){
        return blogRepository.findByAuthor_UserId(Integer.parseInt(currentUserId));
    }

    @Override
    public Long getTotalPostCount(){
        return blogRepository.countByAuthor_UserId(2);
    }
}
