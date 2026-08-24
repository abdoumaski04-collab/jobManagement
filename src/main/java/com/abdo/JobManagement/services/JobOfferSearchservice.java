package com.abdo.JobManagement.services;


import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import com.abdo.JobManagement.entities.user.Admin;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.repositories.JobofferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobOfferSearchservice {

    private final JobofferRepository repo;

    public Page<joboffer> searchjoboffer(String keyword , String location , Long companyid , Pageable pageable, User currentuser){

        if(currentuser instanceof Recruiter recuiter){
            return repo.search(keyword,location,null,companyid,pageable); // status est nulle pas de filtre selon le status
        }
         else if(currentuser instanceof Admin admin){
            return repo.search(keyword,location,null,companyid,pageable); // status est nulle pas de filtre selon le status
        }
        else{
            return repo.search(keyword,location, jobstatus.OPEN,companyid,pageable); // ici on affche seulement les offres dans état opened
        }
    }
}
