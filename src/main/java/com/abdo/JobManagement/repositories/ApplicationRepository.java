package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.application;
import com.abdo.JobManagement.entities.applicationstatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ApplicationRepository extends JpaRepository<application,Long> {

    public boolean existsByCondidatIdAndOfferId(Long CandidateId, Long offerId );

    public boolean existsByOfferId(Long jobofferId);

    public List<application> findAllByCondidatId(Long candidateId);

    @Query("""
select a from application a
where(
a.status= :status
and 
a.offer.id= :jobofferid
)
""")
    public Page<application> getapps(@Param("jobofferid") Long jobofferid, @Param("status") applicationstatus status, Pageable pageable);

    public boolean existsByCondidatIdAndId(Long candidateId,Long AppId);
}
