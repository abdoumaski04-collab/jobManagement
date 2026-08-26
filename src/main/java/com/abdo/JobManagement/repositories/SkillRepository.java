package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<skill,Long> {

    Optional<skill> findByName(String name);
}
