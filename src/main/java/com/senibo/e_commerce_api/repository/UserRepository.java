package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link User} entity.
 * <p>
 * This interface provides standard CRUD operations for User entities and includes
 * custom finder methods for retrieving users by username or email, as well as
 * existence checks for validation.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return an {@link Optional} containing the found user, or {@link Optional#empty()} if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return an {@link Optional} containing the found user, or {@link Optional#empty()} if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by either their email address or their username.
     * This is typically used for login where the user can provide either credential.
     *
     * @param email    The email address to search for.
     * @param username The username to search for.
     * @return an {@link Optional} containing the found user if a match is made on either field, or {@link Optional#empty()} otherwise.
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Checks if a user with the specified username exists in the database.
     * This is an optimized query that is more performant than fetching the entire entity.
     *
     * @param username The username to check for existence.
     * @return {@code true} if a user with the given username exists, {@code false} otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user with the specified email address exists in the database.
     *
     * @param email The email address to check for existence.
     * @return {@code true} if a user with the given email exists, {@code false} otherwise.
     */
    Boolean existsByEmail(String email);

    /**
     * Checks if a user with either the specified email or username exists.
     *
     * @param email    The email address to check.
     * @param username The username to check.
     * @return {@code true} if a user exists with either the given email or username, {@code false} otherwise.
     */
    Boolean existsByEmailOrUsername(String email, String username);
}
