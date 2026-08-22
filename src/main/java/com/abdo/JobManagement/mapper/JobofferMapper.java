package com.abdo.JobManagement.mapper;

import com.abdo.JobManagement.dto.joboffer.JobofferRequest;
import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JobofferMapper {

    public static joboffer toEntity(JobofferRequest request, company company){
        joboffer offer=new joboffer();
        offer.setTitle(request.getTitle());
        offer.setDescription(request.getDescription());
        offer.setLocation(request.getLocation());
        offer.setSalaryrange(request.getSalaryrange());
        offer.setCompany(company);
        return offer;
    }
    public static JobofferResponse toResponse(joboffer offer){
        return new JobofferResponse(offer.getId(), offer.getTitle(), offer.getDescription(), offer.getLocation(), offer.getSalaryrange(), offer.getCompany().getId(),offer.getCreatedat(),offer.getStatus());
    }
}
