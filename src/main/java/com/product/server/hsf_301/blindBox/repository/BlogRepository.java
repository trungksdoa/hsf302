// package: com.product.server.hsf_301.blindBox.repository

package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    // Có thể thêm các phương thức custom nếu cần, ví dụ:
    // List<Blog> findByStatus(String status);
    // Optional<Blog> findByTitle(String title);
    long countByAuthor_UserId(Integer userId);

    Blog getBlogsByAuthor_UserId(Integer authorUserId);

    List<Blog> findAllByAuthor_UserId(Integer authorUserId);

    @Query("select b from Blog b where b.author.userId = ?1")
    Blog findByAuthor_UserId(Integer userId);
}
