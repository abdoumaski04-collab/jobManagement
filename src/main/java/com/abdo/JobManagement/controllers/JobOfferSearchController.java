package com.abdo.JobManagement.controllers;


import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.services.JobOfferSearchservice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/joboffers/search")
public class JobOfferSearchController {

    private final JobOfferSearchservice service;

    @GetMapping
        public ResponseEntity<Page<joboffer>> searchjoboffers(
                @RequestParam(required = false) String keyword,
                @RequestParam(required = false) String location,
                @RequestParam(required = false) Long companyid,
                Pageable pageable,
                @AuthenticationPrincipal User currentuser
                ){
        Page<joboffer> results=service.searchjoboffer(keyword,location,companyid,pageable,currentuser);
        return ResponseEntity.ok(results);
        }
}
