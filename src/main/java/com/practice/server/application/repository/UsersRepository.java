package com.practice.server.application.repository;

import com.practice.server.application.model.enums.Role;
import com.practice.server.application.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByFullName(String fullName);

    Optional<User> findByEmail(String email);

    boolean existsByFullName(String fullName);

    boolean existsByEmail(String email);

    long countByRole(Role role);

}
