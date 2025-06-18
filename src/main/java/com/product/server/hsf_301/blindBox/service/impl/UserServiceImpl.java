package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.AppUser;
import com.product.server.hsf_301.blindBox.repository.UserRepository;
import com.product.server.hsf_301.blindBox.service.UserService;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    
    @Override
    public Page<AppUser> getAllUsers(int page, int size) {
        Pageable pg = PageRequest.of(page,size);
        return userRepository.findAll(pg);
    }

    @Override
    public AppUser getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AppUser createUser(AppUser user) {
        // Encode the password before saving
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    @Override
    public AppUser updateProfile(AppUser user) {
        // Get the existing user
        AppUser existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update only the fields that should be updatable
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        existingUser.setBalance(user.getBalance());

        // Save the updated user
        return userRepository.save(existingUser);
    }

    @Override
    public void updatePassword(Integer userId, String newPassword) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public boolean verifyPassword(Integer userId, String password) {
      return true;
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
