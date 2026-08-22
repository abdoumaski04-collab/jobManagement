package com.abdo.JobManagement.mapper;

import com.abdo.JobManagement.dto.company.CompanyRequest;
import com.abdo.JobManagement.dto.company.CompanyResponse;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.user.Recruiter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CompanyMapper {

    public static company toEntity(CompanyRequest request, Recruiter recruiter){
        company company=new company();
        company.setDescription(request.getDescription());
        company.setName(request.getName());
        company.setWebsite(request.getWebsite());
        company.setOwner(recruiter);
        return company;
    }
    public static CompanyResponse toResponse(company c){
        return new CompanyResponse(c.getId(),c.getName(),c.getDescription(),c.getWebsite(),c.getOwner().getId());
    }
}
