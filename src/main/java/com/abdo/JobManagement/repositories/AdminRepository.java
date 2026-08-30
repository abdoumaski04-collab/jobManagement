package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
