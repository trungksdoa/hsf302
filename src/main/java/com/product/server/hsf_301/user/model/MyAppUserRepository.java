package com.product.server.hsf_301.user.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyAppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);


}
