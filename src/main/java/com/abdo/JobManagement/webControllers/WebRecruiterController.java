package com.abdo.JobManagement.webControllers;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.abdo.JobManagement.dto.company.CompanyRequest;
import com.abdo.JobManagement.dto.joboffer.JobofferRequest;
import com.abdo.JobManagement.dto.joboffer.JobofferResponse;
import com.abdo.JobManagement.entities.application;
import com.abdo.JobManagement.entities.applicationstatus;
import com.abdo.JobManagement.entities.company;
import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.mapper.JobofferMapper;
import com.abdo.JobManagement.repositories.ApplicationRepository;
import com.abdo.JobManagement.repositories.CompanyRepository;
import com.abdo.JobManagement.repositories.JobofferRepository;
import com.abdo.JobManagement.repositories.RecruiterRepo;
import com.abdo.JobManagement.services.ApplicationService;
import com.abdo.JobManagement.services.CompanyService;
import com.abdo.JobManagement.services.JobofferService;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class WebRecruiterController {

    private final CompanyRepository companyRepository;
    private final JobofferRepository jobofferRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterRepo recruiterRepo;
    private final CompanyService companyService;
    private final JobofferService jobofferService;
    private final ApplicationService applicationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User currentUser, Model model) {
        List<company> companies = companyRepository.findByOwnerId(currentUser.getId());
        List<joboffer> offers = jobofferRepository.findAllByCompanyOwnerId(currentUser.getId());

        long totalCompanies = companies.size();
        long totalOffers = offers.size();
        long activeOffers = offers.stream().filter(o -> o.getStatus() == jobstatus.OPEN).count();

        long pendingAppsCount = 0;
        for (joboffer offer : offers) {
            pendingAppsCount += applicationRepository.findByOfferId(offer.getId()).stream()
                    .filter(a -> a.getStatus() == applicationstatus.PENDING)
                    .count();
        }

        List<joboffer> recentOffers = offers.stream()
                .sorted((o1, o2) -> o2.getCreatedat().compareTo(o1.getCreatedat()))
                .limit(5)
                .toList();

        model.addAttribute("totalCompanies", totalCompanies);
        model.addAttribute("totalOffers", totalOffers);
        model.addAttribute("activeOffers", activeOffers);
        model.addAttribute("pendingAppsCount", pendingAppsCount);
        model.addAttribute("recentOffers", recentOffers);

        return "recruiter/dashboard";
    }

    // --- Companies Management ---

    @GetMapping("/companies")
    public String listCompanies(@AuthenticationPrincipal User currentUser, Model model) {
        List<company> companies = companyRepository.findByOwnerId(currentUser.getId());
        model.addAttribute("companies", companies);
        return "recruiter/companies";
    }

    @GetMapping("/companies/new")
    public String newCompanyForm(Model model) {
        model.addAttribute("company", new company());
        return "recruiter/company-form";
    }

    @GetMapping("/companies/edit/{id}")
    public String editCompanyForm(@PathVariable Long id, @AuthenticationPrincipal User currentUser, Model model) {
        company company = companyRepository.findById(id).orElse(null);
        if (company == null || !company.getOwner().getId().equals(currentUser.getId())) {
            return "error/403";
        }
        model.addAttribute("company", company);
        return "recruiter/company-form";
    }

    @PostMapping("/companies/save")
    public String saveCompany(
            @RequestParam(required = false) Long id,
            @RequestParam String name,
            @RequestParam String website,
            @RequestParam String description,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        Recruiter recruiter = recruiterRepo.findById(currentUser.getId()).orElse(null);
        if (recruiter == null) {
            return "redirect:/login";
        }

        try {
            CompanyRequest request = new CompanyRequest(name, description, website);
            if (id == null) {
                companyService.addCompany(request, recruiter);
                redirectAttributes.addFlashAttribute("successMessage", "Entreprise créée avec succès.");
            } else {
                companyService.editcompany(id, request, recruiter);
                redirectAttributes.addFlashAttribute("successMessage", "Entreprise mise à jour avec succès.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/recruiter/companies";
    }

    @PostMapping("/companies/delete/{id}")
    public String deleteCompany(@PathVariable Long id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        Recruiter recruiter = recruiterRepo.findById(currentUser.getId()).orElse(null);
        if (recruiter == null) {
            return "redirect:/login";
        }

        try {
            companyService.deleteComapany(id, recruiter);
            redirectAttributes.addFlashAttribute("successMessage", "Entreprise supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/recruiter/companies";
    }

    // --- Job Offers Management ---

    @GetMapping("/offers")
    public String listOffers(@AuthenticationPrincipal User currentUser, Model model) {
        List<joboffer> offers = jobofferRepository.findAllByCompanyOwnerId(currentUser.getId());
        model.addAttribute("offers", offers);
        return "recruiter/offers";
    }

    @GetMapping("/offers/new")
    public String newOfferForm(
            @RequestParam(required = false) Long companyId,
            @AuthenticationPrincipal User currentUser,
            Model model
    ) {
        List<company> companies = companyRepository.findByOwnerId(currentUser.getId());
        model.addAttribute("companies", companies);
        model.addAttribute("selectedCompanyId", companyId);
        model.addAttribute("offer", new joboffer());
        return "recruiter/offer-form";
    }

    @GetMapping("/offers/edit/{id}")
    public String editOfferForm(@PathVariable Long id, @AuthenticationPrincipal User currentUser, Model model) {
        joboffer offer = jobofferRepository.findById(id).orElse(null);
        if (offer == null || !offer.getCompany().getOwner().getId().equals(currentUser.getId())) {
            return "error/403";
        }
        List<company> companies = companyRepository.findByOwnerId(currentUser.getId());
        model.addAttribute("companies", companies);
        model.addAttribute("offer", offer);
        return "recruiter/offer-form";
    }

    @PostMapping("/offers/save")
    public String saveOffer(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long companyId,
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam String salaryrange,
            @RequestParam String description,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        Recruiter recruiter = recruiterRepo.findById(currentUser.getId()).orElse(null);
        if (recruiter == null) {
            return "redirect:/login";
        }

        try {
            if (id == null) {
                if (companyId == null) {
                    throw new IllegalArgumentException("Veuillez sélectionner une entreprise de rattachement.");
                }
                JobofferRequest req = new JobofferRequest(title, description, location, salaryrange, companyId);
                company comp = companyRepository.findById(companyId).orElseThrow(() -> new IllegalArgumentException("Entreprise non trouvée"));
                jobofferService.addoffer(comp, recruiter, req);
                redirectAttributes.addFlashAttribute("successMessage", "Offre d'emploi publiée avec succès !");
            } else {
                JobofferRequest req = new JobofferRequest(title, description, location, salaryrange, companyId);
                jobofferService.editoffer(id, recruiter, req);
                redirectAttributes.addFlashAttribute("successMessage", "Offre d'emploi mise à jour avec succès !");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/recruiter/offers";
    }

    @PostMapping("/offers/delete/{id}")
    public String deleteOffer(@PathVariable Long id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        Recruiter recruiter = recruiterRepo.findById(currentUser.getId()).orElse(null);
        if (recruiter == null) {
            return "redirect:/login";
        }

        try {
            joboffer offer = jobofferRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offre non trouvée"));
            jobofferService.deleteoffer(id, recruiter, offer.getCompany());
            redirectAttributes.addFlashAttribute("successMessage", "Offre supprimée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/recruiter/offers";
    }

    @PostMapping("/offers/{id}/toggle-status")
    public String toggleOfferStatus(@PathVariable Long id, @AuthenticationPrincipal User currentUser, RedirectAttributes redirectAttributes) {
        Recruiter recruiter = recruiterRepo.findById(currentUser.getId()).orElse(null);
        if (recruiter == null) {
            return "redirect:/login";
        }

        try {
            joboffer offer = jobofferRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offre non trouvée"));
            if (offer.getStatus() == jobstatus.OPEN) {
                jobofferService.closeOffer(id, recruiter);
                redirectAttributes.addFlashAttribute("successMessage", "L'offre a été fermée aux candidatures.");
            } else {
                jobofferService.openOffer(id, recruiter);
                redirectAttributes.addFlashAttribute("successMessage", "L'offre a été rouverte.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/recruiter/offers";
    }

    // --- Review Applications for an Offer ---

    @GetMapping("/offers/{id}/applications")
    public String viewApplications(@PathVariable Long id, @AuthenticationPrincipal User currentUser, Model model) {
        joboffer offer = jobofferRepository.findById(id).orElse(null);
        if (offer == null || !offer.getCompany().getOwner().getId().equals(currentUser.getId())) {
            return "error/403";
        }

        List<application> applications = applicationRepository.findByOfferId(id);
        model.addAttribute("offer", offer);
        model.addAttribute("applications", applications);

        return "recruiter/applications";
    }

    @PostMapping("/applications/{id}/accept")
    public String acceptApplication(
            @PathVariable Long id,
            @RequestParam Long offerId,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            applicationService.acceptApplication(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Candidature acceptée ! Un email de confirmation a été envoyé au candidat.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/recruiter/offers/" + offerId + "/applications";
    }

    @PostMapping("/applications/{id}/refuse")
    public String refuseApplication(
            @PathVariable Long id,
            @RequestParam Long offerId,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            applicationService.refuseApplication(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Candidature refusée. Un email a été envoyé au candidat.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/recruiter/offers/" + offerId + "/applications";
    }
}
