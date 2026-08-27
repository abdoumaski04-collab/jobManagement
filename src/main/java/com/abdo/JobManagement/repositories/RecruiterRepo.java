package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.user.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterRepo extends JpaRepository<Recruiter,Long> {
}
