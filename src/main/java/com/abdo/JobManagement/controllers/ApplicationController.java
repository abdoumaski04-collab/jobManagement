package com.abdo.JobManagement.controllers;


import com.abdo.JobManagement.dto.application.ApplicationResponse;
import com.abdo.JobManagement.dto.application.MyApplicationsResponse;
import com.abdo.JobManagement.entities.application;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.services.ApplicationService;
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
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService appser;

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> addApplication(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ){
        ApplicationResponse response=appser.addApplication(user,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/myapps")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<MyApplicationsResponse> getMyapps(@AuthenticationPrincipal User user){
        MyApplicationsResponse response=appser.getMyApps(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{appId}/annuler")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> annulerApp(@AuthenticationPrincipal User user,@PathVariable long appId){
        ApplicationResponse response=appser.annulerApp(user,appId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{jobofferid}/allapps")
    @PreAuthorize("hasRole('RECRUITER')")
            public ResponseEntity<Page<application>> getappspending(@PathVariable Long jobofferid,
                                                                    @AuthenticationPrincipal User user,
                                                                    Pageable pageable){
        Page<application> pages=appser.getappspending(jobofferid,user,pageable);
        return ResponseEntity.ok(pages);
    }

    @PutMapping("{idApp}/refuse")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApplicationResponse> refuseApplication(@PathVariable Long idApp,
                                                                 @AuthenticationPrincipal User user){
        ApplicationResponse app=appser.refuseApplication(idApp,user);
        return ResponseEntity.ok(app);
    }

    @PutMapping("{idApp}/accept")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApplicationResponse> acceptApplication(@PathVariable Long idApp,
                                                                 @AuthenticationPrincipal User user){
        ApplicationResponse app=appser.acceptApplication(idApp,user);
        return ResponseEntity.ok(app);
    }
}

