package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.User;
import java.util.List;

public interface UserService {
    
    List<User> getAllUsers();
    
    User getUserById(Integer id);
    
    User getUserByUsername(String username);
    
    User getUserByEmail(String email);
    
    User createUser(User user);
    
    User updateProfile(User user);
    
    void updatePassword(Integer userId, String newPassword);
    
    boolean verifyPassword(Integer userId, String password);
    
    void deleteUser(Integer userId);
    public User findByUsername(String username);
}
