// package: com.product.server.hsf_301.blindBox.repository

package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    // Có thể thêm các phương thức custom nếu cần, ví dụ:
    // List<Blog> findByStatus(String status);
    // Optional<Blog> findByTitle(String title);
}
