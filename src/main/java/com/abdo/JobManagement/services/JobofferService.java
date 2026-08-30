package com.abdo.JobManagement.services;


import com.abdo.JobManagement.dto.joboffer.JobofferRequest;
import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.mapper.JobofferMapper;
import com.abdo.JobManagement.repositories.JobofferRepository;
import com.abdo.JobManagement.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobofferService {

    private final JobofferRepository repo;
    private final UserRepository userrepo;

    @Transactional
    public Page<JobofferResponse> getAllMyOffers(User user, Pageable pageable){
        Long iduser=user.getId();
        userrepo.findById(iduser).orElseThrow(()->new RessourceNotFoundException("user not found"));

        Page<joboffer> response=repo.findByCompanyOwnerId(iduser, pageable);
        return  response.map(JobofferMapper::toResponse);
    }

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

    @Transactional
    public JobofferResponse closeOffer(Long id, Recruiter recruiter){
        joboffer existing=repo.findById(id).orElseThrow(()->new RessourceNotFoundException("offer not found"));
        if(!(existing.getCompany().getOwner().getId().equals(recruiter.getId()))){
            throw new AccessDeniedException("you are not the owner of this company to close offer");
        }
        if(existing.getStatus()==jobstatus.CLOSED){
            throw new IllegalArgumentException("cette offre est déjà fermée");
        }
        existing.setStatus(jobstatus.CLOSED);
        repo.save(existing);
        return JobofferMapper.toResponse(existing);
    }

    @Transactional
    public JobofferResponse openOffer(Long id, Recruiter recruiter){
        joboffer existing=repo.findById(id).orElseThrow(()->new RessourceNotFoundException("offer not found"));
        if(!(existing.getCompany().getOwner().getId().equals(recruiter.getId()))){
            throw new AccessDeniedException("you are not the owner of this company to open offer");
        }
        if(existing.getStatus()==jobstatus.OPEN){
            throw new IllegalArgumentException("cette offre est déjà ouverte");
        }
        existing.setStatus(jobstatus.OPEN);
        repo.save(existing);
        return JobofferMapper.toResponse(existing);
    }
}