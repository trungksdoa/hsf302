package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findByUsername(String username);
    
    User findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
