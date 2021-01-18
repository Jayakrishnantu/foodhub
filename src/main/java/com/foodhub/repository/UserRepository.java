package com.foodhub.repository;

import com.foodhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Users
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // derived method - findByUserName
    Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String username);
}
