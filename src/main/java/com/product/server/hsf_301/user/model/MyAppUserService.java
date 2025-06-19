package com.product.server.hsf_301.user.model;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MyAppUserService implements UserDetailsService {

    @Autowired
    private  MyAppUserRepository repository;


    //public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<MyAppUser> user = repository.findByUsername(username);
//        if (user.isPresent()) {
//            var userObj = user.get();
//            return User.builder()
//                    .username(userObj.getUsername())
//                    .password(userObj.getPassword())
//                    .roles(userObj.getRole())
//                    .build();
//        }else{
//            throw new UsernameNotFoundException(username);
//        }


        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<com.product.server.hsf_301.user.model.User> user = repository.findByUsername(username);
            if (user.isPresent()) {
              com.product.server.hsf_301.user.model.User userObj = user.get();
                String role = "ADMIN".equalsIgnoreCase(userObj.getRole()) ? "ADMIN" : "USER";
                return User.builder()
                        .username(userObj.getUsername())
                        .password(userObj.getPassword())
                        .roles(role)
                        .build();
            }
            throw new UsernameNotFoundException("User not found: " + username);
        }

}