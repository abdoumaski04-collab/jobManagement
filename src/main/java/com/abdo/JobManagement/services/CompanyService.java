package com.abdo.JobManagement.services;


import com.abdo.JobManagement.dto.company.CompanyRequest;
import com.abdo.JobManagement.dto.company.CompanyResponse;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.mapper.CompanyMapper;
import com.abdo.JobManagement.repositories.CompanyRepository;
import com.abdo.JobManagement.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyrepo;
    private final UserRepository userrepo;

    public void checkownership(Recruiter recruiter,company company){
        if(!recruiter.getId().equals(company.getOwner().getId()))
            throw new AccessDeniedException("you not the owner of this company so you don't have the acess");
    }

    @Transactional
    public CompanyResponse getCompany(Long id){
        Optional<company> company=companyrepo.findById(id);
        return company.map(CompanyMapper::toResponse)
                .orElseThrow(()->new RessourceNotFoundException("company not found"));
    }

    @Transactional
    public Page<CompanyResponse> getAll(User user, Pageable pageable){
        Long iduser=user.getId();
        userrepo.findById(iduser).orElseThrow(()->new RessourceNotFoundException("user not found"));

        Page<company> response=companyrepo.findAll(pageable);
        return  response.map(CompanyMapper::toResponse);
    }

    @Transactional
    public CompanyResponse addCompany(CompanyRequest request, Recruiter recruiter){
        company company=CompanyMapper.toEntity(request,recruiter);
        companyrepo.save(company);
        return CompanyMapper.toResponse(company);
    }

    @Transactional
    public void deleteComapany(Long id,Recruiter recruiter){
        company company=companyrepo.findById(id).orElseThrow(()->new RessourceNotFoundException("company not found"));
        checkownership(recruiter,company);
        companyrepo.deleteById(id);
    }

    @Transactional
    public CompanyResponse editcompany(Long id, CompanyRequest request, Recruiter recruiter) {
        company existing = companyrepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("company not found"));

        checkownership(recruiter, existing);   // <-- vérifie contre l'entité existante, pas contre un objet neuf

        existing.setName(request.getName());
        existing.setWebsite(request.getWebsite());
        existing.setDescription(request.getDescription());
        // pas de existing.setOwner(recruiter) : le owner ne doit jamais changer lors d'un edit

        companyrepo.save(existing);
        return CompanyMapper.toResponse(existing);
    }
}
