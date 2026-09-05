package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobofferRepository extends JpaRepository<joboffer, Long>, JpaSpecificationExecutor<joboffer> {

    default Page<joboffer> search(String keyword, String location, jobstatus status, Long companyid, Pageable pageable) {
        return findAll(JobofferSpecification.filter(keyword, location, status, companyid), pageable);
    }

    List<joboffer> findByCompanyId(Long id);

    @Query("SELECT j FROM joboffer j WHERE j.company.owner.id = :ownerId ORDER BY j.createdat DESC")
    List<joboffer> findAllByCompanyOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT j FROM joboffer j WHERE j.company.owner.id = :ownerId")
    Page<joboffer> findByCompanyOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);
}