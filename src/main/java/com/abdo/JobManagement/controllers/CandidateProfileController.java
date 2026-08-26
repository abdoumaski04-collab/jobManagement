package com.abdo.JobManagement.controllers;


import com.abdo.JobManagement.dto.CandidateProfile.CandidatePofileResponse;
import com.abdo.JobManagement.dto.CandidateProfile.updateCandidateProfileRequest;
import com.abdo.JobManagement.dto.skill.AddSkillRequest;
import com.abdo.JobManagement.dto.skill.SkillsResponse;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.services.CandidateProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateProfileController {

    private final CandidateProfileService service;

    @GetMapping("/update")
    public ResponseEntity<CandidatePofileResponse> updateprofile(
            @Valid @RequestBody updateCandidateProfileRequest request,
            @AuthenticationPrincipal User user
    ) {
        return service.updateprofile(request, user);
    }

    @GetMapping()
    public ResponseEntity<CandidatePofileResponse> updateprofile(
            @AuthenticationPrincipal User user
    ) {
        return service.getProfile(user);
    }

    @GetMapping("/skills")
    public ResponseEntity<SkillsResponse> getskills(@AuthenticationPrincipal User candidate) {
        return service.getskills(candidate);
    }

    @PostMapping("/skills/add")
    public ResponseEntity<SkillsResponse> addskill(
            @Valid @RequestBody AddSkillRequest request,
            @AuthenticationPrincipal User candidate
    ) {
    return service.addskill(request,candidate);

    }
}
