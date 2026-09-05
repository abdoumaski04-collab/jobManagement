package com.abdo.JobManagement.webControllers;

import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.mapper.JobofferMapper;
import com.abdo.JobManagement.repositories.*;
import com.abdo.JobManagement.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class WebAdminController {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterRepo recruiterRepo;
    private final CompanyRepository companyRepository;
    private final JobofferRepository jobofferRepository;
    private final ApplicationRepository applicationRepository;
    private final AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalUsers = userRepository.count();
        long totalCandidates = candidateProfileRepository.count();
        long totalRecruiters = recruiterRepo.count();
        long totalCompanies = companyRepository.count();
        long totalOffers = jobofferRepository.count();
        long totalApplications = applicationRepository.count();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalCandidates", totalCandidates);
        model.addAttribute("totalRecruiters", totalRecruiters);
        model.addAttribute("totalCompanies", totalCompanies);
        model.addAttribute("totalOffers", totalOffers);
        model.addAttribute("totalApplications", totalApplications);

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(
            @RequestParam(defaultValue = "ALL") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<? extends User> usersPage;

        if ("CONDIDATE".equalsIgnoreCase(role) || "CANDIDATE".equalsIgnoreCase(role)) {
            usersPage = candidateProfileRepository.findAll(pageable);
        } else if ("RECRUITER".equalsIgnoreCase(role)) {
            usersPage = recruiterRepo.findAll(pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("selectedRole", role);

        return "admin/users";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "ALL") String role,
            @AuthenticationPrincipal User admin,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminService.accountActivate(admin, id);
            redirectAttributes.addFlashAttribute("successMessage", "Compte activé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/admin/users?role=" + role;
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "ALL") String role,
            @AuthenticationPrincipal User admin,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminService.accountDeactivate(admin, id);
            redirectAttributes.addFlashAttribute("successMessage", "Compte désactivé.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/admin/users?role=" + role;
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "ALL") String role,
            @AuthenticationPrincipal User admin,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminService.deleteUser(admin, id);
            redirectAttributes.addFlashAttribute("successMessage", "Utilisateur supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/users?role=" + role;
    }

    // --- Companies Management ---

    @GetMapping("/companies")
    public String listCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<company> pageResult = companyRepository.findAll(pageable);

        model.addAttribute("companies", pageResult.getContent());
        model.addAttribute("currentPage", pageResult.getNumber());
        model.addAttribute("totalPages", pageResult.getTotalPages());

        return "admin/companies";
    }

    @PostMapping("/companies/{id}/delete")
    public String deleteCompany(@PathVariable Long id, @AuthenticationPrincipal User admin, RedirectAttributes redirectAttributes) {
        try {
            adminService.CompanyDelete(admin, id);
            redirectAttributes.addFlashAttribute("successMessage", "Entreprise et données associées supprimées.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/companies";
    }

    // --- Offers Management ---

    @GetMapping("/offers")
    public String listOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdat").descending());
        Page<joboffer> pageResult = jobofferRepository.findAll(pageable);

        model.addAttribute("offers", pageResult.getContent());
        model.addAttribute("currentPage", pageResult.getNumber());
        model.addAttribute("totalPages", pageResult.getTotalPages());

        return "admin/offers";
    }

    @PostMapping("/offers/{id}/delete")
    public String deleteOffer(@PathVariable Long id, @AuthenticationPrincipal User admin, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteJoboffer(admin, id);
            redirectAttributes.addFlashAttribute("successMessage", "Offre d'emploi supprimée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/admin/offers";
    }
}
