package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername(String email, String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByEmailOrUsername(String email, String username);
}
