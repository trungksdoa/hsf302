package com.product.server.hsf_301.user.controller;

import com.product.server.hsf_301.user.model.AppUser;
import com.product.server.hsf_301.user.model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AppUser createUser(@RequestBody AppUser user) {
         //ma hoa mat khau truoc khi luu vao database
         user.setPassword(passwordEncoder.encode(user.getPassword()));
         user.setBalance(BigDecimal.ZERO);
         return myAppUserRepository.save(user);

    }


}
