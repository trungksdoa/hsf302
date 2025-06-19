package com.product.server.hsf_301.user.controller;

import com.product.server.hsf_301.user.model.MyAppUserRepository;
import com.product.server.hsf_301.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

     @PostMapping(value="/req/signup", consumes = "application/json")
    public User createUser(@RequestBody User user) {
         //ma hoa mat khau truoc khi luu vao database
         user.setPassword(passwordEncoder.encode(user.getPassword()));
         return myAppUserRepository.save(user);

    }


}
