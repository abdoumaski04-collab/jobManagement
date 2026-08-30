package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobofferRepository extends JpaRepository<joboffer, Long> {

    @Query("""
        SELECT j FROM joboffer j
        WHERE (
            :keyword IS NULL
            OR LOWER(j.title) LIKE LOWER(CONCAT('%', cast(:keyword as String ), '%'))
            OR LOWER(j.description) LIKE LOWER(CONCAT('%', cast(:keyword as String ), '%'))
        )
        AND (
            :location IS NULL
            OR LOWER(j.location) LIKE LOWER(CONCAT('%', cast(:location as String ), '%'))
        )
        AND (
            :status IS NULL
            OR j.status = :status
        )
        AND (
            :companyid IS NULL
            OR j.company.id = :companyid
        )
        """)
    Page<joboffer> search(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("status") jobstatus status,
            @Param("companyid") Long companyid,
            Pageable pageable
    );

    List<joboffer> findByCompanyId(Long id);

    Page<joboffer> findByCompanyOwnerId(Long ownerId, Pageable pageable);
}