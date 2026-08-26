package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.user.Condidateprofile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateProfileRepository extends JpaRepository<Condidateprofile,Long> {
}
