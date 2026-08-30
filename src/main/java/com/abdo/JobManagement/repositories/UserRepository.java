package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.dto.Admin.UserResponse;
import com.abdo.JobManagement.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User>  findByEmail(String email);
    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
