package com.abdo.JobManagement.webControllers;

import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import com.abdo.JobManagement.repositories.CandidateProfileRepository;
import com.abdo.JobManagement.repositories.CompanyRepository;
import com.abdo.JobManagement.repositories.JobofferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebHomeController {

    private final JobofferRepository jobofferRepository;
    private final CompanyRepository companyRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        long totalOffers = jobofferRepository.count();
        long totalCompanies = companyRepository.count();
        long totalCandidates = candidateProfileRepository.count();

        // Get 6 most recent open offers for preview
        Pageable pageable = PageRequest.of(0, 6, Sort.by("createdat").descending());
        Page<joboffer> recentOffersPage = jobofferRepository.search(null, null, jobstatus.OPEN, null, pageable);
        List<joboffer> recentOffers = recentOffersPage.getContent();

        model.addAttribute("totalOffers", totalOffers);
        model.addAttribute("totalCompanies", totalCompanies);
        model.addAttribute("totalCandidates", totalCandidates);
        model.addAttribute("recentOffers", recentOffers);

        return "home";
    }
}
