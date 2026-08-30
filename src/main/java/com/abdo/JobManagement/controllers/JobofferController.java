package com.abdo.JobManagement.controllers;

import com.abdo.JobManagement.dto.joboffer.JobofferRequest;
import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import com.abdo.JobManagement.repositories.CompanyRepository;
import com.abdo.JobManagement.repositories.JobofferRepository;
import com.abdo.JobManagement.services.JobofferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/joboffers")
public class JobofferController {

    private final JobofferService service;
    private final CompanyRepository companyRepo;
    private final JobofferRepository jobofferRepo;

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping()
    public ResponseEntity<Page<JobofferResponse>> getAllMyoffers(@AuthenticationPrincipal User user, Pageable pageable){
        return ResponseEntity.ok(service.getAllMyOffers(user,pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobofferResponse> getoffer(@PathVariable Long id){
        JobofferResponse response = service.getoffer(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/add")
    public ResponseEntity<JobofferResponse> addoffer(@Valid @RequestBody JobofferRequest request,
                                                     @AuthenticationPrincipal Recruiter recruiter){
        company company = companyRepo.findById(request.getCompanyId())
                .orElseThrow(() -> new RessourceNotFoundException("company not found"));
        JobofferResponse response = service.addoffer(company, recruiter, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobofferResponse> editoffer(@PathVariable Long id,
                                                      @AuthenticationPrincipal Recruiter recruiter,
                                                      @Valid @RequestBody JobofferRequest request){
        return ResponseEntity.ok(service.editoffer(id,recruiter,request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteoffer(@PathVariable Long id,
                                            @AuthenticationPrincipal Recruiter recruiter){
        joboffer offer = jobofferRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("offer not found"));
        service.deleteoffer(id, recruiter, offer.getCompany());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/close/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobofferResponse> closeoffer(@PathVariable Long id,
                                                       @AuthenticationPrincipal Recruiter recruiter){
        return ResponseEntity.ok(service.closeOffer(id, recruiter));
    }

    @PatchMapping("/open/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobofferResponse> openoffer(@PathVariable Long id,
                                                      @AuthenticationPrincipal Recruiter recruiter){
        return ResponseEntity.ok(service.openOffer(id, recruiter));
    }
}