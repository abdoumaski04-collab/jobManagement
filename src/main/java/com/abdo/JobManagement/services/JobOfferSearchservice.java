package com.abdo.JobManagement.services;


import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import com.abdo.JobManagement.entities.user.Admin;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.mapper.JobofferMapper;
import com.abdo.JobManagement.repositories.JobofferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobOfferSearchservice {

    private final JobofferRepository repo;

    public Page<JobofferResponse> searchjoboffer(String keyword, String location, Long companyid, Pageable pageable, User currentuser) {
        Page<joboffer> pages;
        if (currentuser instanceof Recruiter recuiter) {
            pages= repo.search(keyword, location, null, companyid, pageable); // status est nulle pas de filtre selon le status
        } else if (currentuser instanceof Admin admin) {
            pages= repo.search(keyword, location, null, companyid, pageable); // status est nulle pas de filtre selon le status
        } else {
            pages= repo.search(keyword, location, jobstatus.OPEN, companyid, pageable); // ici on affche seulement les offres dans état opened
        }
        return pages.map(JobofferMapper::toResponse);
    }
}
