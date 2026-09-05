package com.abdo.JobManagement.webControllers;

import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.repositories.ApplicationRepository;
import com.abdo.JobManagement.repositories.CandidateProfileRepository;
import com.abdo.JobManagement.repositories.CompanyRepository;
import com.abdo.JobManagement.repositories.JobofferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebJobController {

    private final JobofferRepository jobofferRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    @Value("${app.cv.upload-dir:docs/uploads/cvs}")
    private String uploadDir;

    @GetMapping("/jobs")
    public String listJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @AuthenticationPrincipal User currentUser,
            Model model
    ) {
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String cleanLocation = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdat").descending());
        jobstatus statusFilter = null;
        if (currentUser == null || currentUser instanceof Condidateprofile) {
            statusFilter = jobstatus.OPEN;
        }
        Page<joboffer> offerPage = jobofferRepository.search(cleanKeyword, cleanLocation, statusFilter, companyId, pageable);

        List<company> companies = companyRepository.findAll();

        model.addAttribute("offers", offerPage.getContent());
        model.addAttribute("currentPage", offerPage.getNumber());
        model.addAttribute("totalPages", offerPage.getTotalPages());
        model.addAttribute("totalElements", offerPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);
        model.addAttribute("companyId", companyId);
        model.addAttribute("companies", companies);

        return "jobs/list";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            Model model
    ) {
        joboffer offer = jobofferRepository.findById(id).orElse(null);
        if (offer == null) {
            return "error/404";
        }

        model.addAttribute("offer", offer);
        model.addAttribute("company", offer.getCompany());

        boolean alreadyApplied = false;
        boolean hasCv = false;

        if (currentUser != null) {
            Condidateprofile candidate = candidateProfileRepository.findById(currentUser.getId()).orElse(null);
            if (candidate != null) {
                alreadyApplied = applicationRepository.existsByCondidatIdAndOfferId(candidate.getId(), offer.getId());
                hasCv = (candidate.getCvurl() != null && !candidate.getCvurl().isBlank());
            }
        }

        model.addAttribute("alreadyApplied", alreadyApplied);
        model.addAttribute("hasCv", hasCv);

        return "jobs/detail";
    }

    @GetMapping("/files/cv/{filename}")
    public ResponseEntity<Resource> serveCvFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists() || !file.canRead()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
