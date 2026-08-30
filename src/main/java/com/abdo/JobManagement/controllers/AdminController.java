package com.abdo.JobManagement.controllers;

import com.abdo.JobManagement.dto.Admin.UserResponse;
import com.abdo.JobManagement.dto.company.CompanyResponse;
import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.dto.message.MessageResponse;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/companies")
    public Page<CompanyResponse> getAllCompanies(@AuthenticationPrincipal User admin, Pageable pageable) {
        return adminService.getAllCompanies(admin, pageable);
    }

    @DeleteMapping("/companies/{companyId}")
    public MessageResponse deleteCompany(@AuthenticationPrincipal User admin, @PathVariable Long companyId) {
        return adminService.CompanyDelete(admin, companyId);
    }

    @GetMapping("/users")
    public Page<?> getUsers(@AuthenticationPrincipal User admin,
                            Pageable pageable,
                            @RequestParam(defaultValue = "ALL") String role) {
        return adminService.getUser(admin, pageable, role);
    }

    @DeleteMapping("/users/{userId}")
    public MessageResponse deleteUser(@AuthenticationPrincipal User admin, @PathVariable Long userId) {
        return adminService.deleteUser(admin, userId);
    }

    @PatchMapping("/users/{userId}/activate")
    public UserResponse activateUser(@AuthenticationPrincipal User admin, @PathVariable Long userId) {
        return adminService.accountActivate(admin, userId);
    }

    @PatchMapping("/users/{userId}/deactivate")
    public UserResponse deactivateUser(@AuthenticationPrincipal User admin, @PathVariable Long userId) {
        return adminService.accountDeactivate(admin, userId);
    }

    @GetMapping("/joboffers")
    public ResponseEntity<Page<JobofferResponse>> getAlloffers(@AuthenticationPrincipal User user, Pageable pageable){
        return ResponseEntity.ok(adminService.getAll(user,pageable));
    }

    @GetMapping("/joboffers/{jobofferid}")
    public ResponseEntity<MessageResponse> deleteoffer(@AuthenticationPrincipal User user,@PathVariable Long jobofferid){
        return ResponseEntity.ok(adminService.deleteJoboffer(user,jobofferid));
    }
}