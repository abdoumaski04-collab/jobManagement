package com.abdo.JobManagement.controllers;


import com.abdo.JobManagement.dto.company.CompanyRequest;
import com.abdo.JobManagement.dto.company.CompanyResponse;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.services.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyservice;


    @GetMapping("/select/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long id){
        return ResponseEntity.ok(companyservice.getCompany(id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<CompanyResponse> addCompany(
            @Valid @RequestBody CompanyRequest request,
            @AuthenticationPrincipal Recruiter recruiter
            ){
        CompanyResponse response=companyservice.addCompany(request,recruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<CompanyResponse> editcompany(@PathVariable Long id,
                                                       @AuthenticationPrincipal Recruiter recruiter,
                                                       @Valid @RequestBody CompanyRequest request){
        return ResponseEntity.ok(companyservice.editcompany(id,request,recruiter));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long id,
            @AuthenticationPrincipal Recruiter recruiter
    ){
        companyservice.deleteComapany(id,recruiter);
        return ResponseEntity.noContent().build();
    }
}
