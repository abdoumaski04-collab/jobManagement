package com.abdo.JobManagement.controllers;

import com.abdo.JobManagement.dto.cv.cvResponse;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.services.CvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE')")
@RequestMapping("/api/profile")
public class CvController {

    private final CvService cvser;

    @PostMapping("cv")
    public ResponseEntity<cvResponse> uploadcv(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
            ){
        String url=cvser.uploadcv(file,user);
        return ResponseEntity.ok(new cvResponse(url));
    }

}
