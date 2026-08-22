package com.abdo.JobManagement.services;


import com.abdo.JobManagement.dto.joboffer.JobofferRequest;
import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.mapper.JobofferMapper;
import com.abdo.JobManagement.repositories.JobofferRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobofferService {

    private final JobofferRepository repo;

    @Transactional
    public JobofferResponse getoffer(Long id){
        joboffer joboffer=repo.findById(id).orElseThrow(()-> new RessourceNotFoundException("offer not found"));
        return JobofferMapper.toResponse(joboffer);
    }

    @Transactional
    public JobofferResponse addoffer(company company, Recruiter recruiter, JobofferRequest request){

        if(!(company.getOwner().getId().equals(recruiter.getId()))){
            throw new AccessDeniedException("you are not the owner of this company to share offer");
        }
        joboffer offer=JobofferMapper.toEntity(request,company);
        repo.save(offer);
        return JobofferMapper.toResponse(offer);
    }

    @Transactional
    public  JobofferResponse editoffer(Long id,Recruiter recruiter,JobofferRequest request){
        joboffer existing=repo.findById(id).orElseThrow(()->new RessourceNotFoundException("offer not found"));
        if(!(existing.getCompany().getOwner().getId().equals(recruiter.getId()))){
            throw new AccessDeniedException("you are not the owner of this company to share offer");
        }


        joboffer newoffer=JobofferMapper.toEntity(request,existing.getCompany());
        existing.setTitle(newoffer.getTitle());
        existing.setDescription(newoffer.getDescription());
        existing.setSalaryrange(newoffer.getSalaryrange());

        repo.save(existing);

        return JobofferMapper.toResponse(existing);
    }

    @Transactional
    public void deleteoffer(Long id,Recruiter recruiter,company company){
        if(!(company.getOwner().getId().equals(recruiter.getId()))){
            throw new AccessDeniedException("you are not the owner of this company to share offer");
        }

        joboffer existing=repo.findById(id).orElseThrow(()->new RessourceNotFoundException("offer not found"));


        repo.deleteById(id);

    }
}