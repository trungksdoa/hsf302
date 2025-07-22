package com.product.server.hsf_301.user.controller;

import com.product.server.hsf_301.user.model.ApiResponse;
import com.product.server.hsf_301.user.model.AppUser;
import com.product.server.hsf_301.user.model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class RegistrationController {

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping(value="/req/signup", consumes = "application/json")
    public ResponseEntity<ApiResponse> createUser(@RequestBody AppUser user) {
        try {
            // Check if email already exists
            if (myAppUserRepository.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Email already exists", false, null));
            }

            // Proceed with user creation
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setBalance(BigDecimal.ZERO);
            AppUser savedUser = myAppUserRepository.save(user);

            return ResponseEntity.ok(new ApiResponse("Registration successful", true, savedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Registration failed", false, null));
        }
    }


}
