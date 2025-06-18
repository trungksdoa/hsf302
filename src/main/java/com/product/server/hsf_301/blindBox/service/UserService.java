package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.AppUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<AppUser> getAllUsers(int page, int size);

    AppUser getUserById(Integer id);

    AppUser getUserByUsername(String username);

    AppUser getUserByEmail(String email);

    AppUser createUser(AppUser user);

    AppUser updateProfile(AppUser user);

    void updatePassword(Integer userId, String newPassword);

    boolean verifyPassword(Integer userId, String password);

    void deleteUser(Integer userId);
    public AppUser findByUsername(String username);
}
