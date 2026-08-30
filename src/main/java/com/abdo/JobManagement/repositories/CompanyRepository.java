package com.abdo.JobManagement.repositories;


import com.abdo.JobManagement.entities.company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<company,Long> {

    Page<company> findAll(Pageable pageable);
    List<company> findByOwnerId(Long id);
}
