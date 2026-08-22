package com.abdo.JobManagement.repositories;


import com.abdo.JobManagement.entities.company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<company,Long> {

}
