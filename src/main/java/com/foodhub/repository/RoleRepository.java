package com.foodhub.repository;

import com.foodhub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for User Roles
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
