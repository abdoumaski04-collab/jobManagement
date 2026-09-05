package com.abdo.JobManagement.webControllers;

import com.abdo.JobManagement.dto.CandidateProfile.updateCandidateProfileRequest;
import com.abdo.JobManagement.entities.application;
import com.abdo.JobManagement.entities.applicationstatus;
import com.abdo.JobManagement.entities.skill;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.repositories.ApplicationRepository;
import com.abdo.JobManagement.repositories.CandidateProfileRepository;
import com.abdo.JobManagement.repositories.SkillRepository;
import com.abdo.JobManagement.repositories.UserRepository;
import com.abdo.JobManagement.services.ApplicationService;
import com.abdo.JobManagement.services.CandidateProfileService;
import com.abdo.JobManagement.services.CvService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class WebCandidateController {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final SkillRepository skillRepository;
    private final CandidateProfileService candidateProfileService;
    private final ApplicationService applicationService;
    private final CvService cvService;

    private String extractFilename(String cvUrl) {
        if (cvUrl == null || cvUrl.isBlank()) return null;
        File f = new File(cvUrl);
        return f.getName();
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User currentUser, Model model) {
        Condidateprofile profile = candidateProfileRepository.findById(currentUser.getId()).orElse(null);
        if (profile == null) {
            return "redirect:/login";
        }

        List<application> applications = applicationRepository.findAllByCondidatId(currentUser.getId());
        long totalApps = applications.size();
        long pendingApps = applications.stream().filter(a -> a.getStatus() == applicationstatus.PENDING).count();
        long acceptedApps = applications.stream().filter(a -> a.getStatus() == applicationstatus.ACCEPTED).count();

        List<application> recentApps = applications.stream()
                .sorted((a1, a2) -> a2.getAppliedat().compareTo(a1.getAppliedat()))
                .limit(5)
                .toList();

        model.addAttribute("user", currentUser);
        model.addAttribute("profile", profile);
        model.addAttribute("totalApps", totalApps);
        model.addAttribute("pendingApps", pendingApps);
        model.addAttribute("acceptedApps", acceptedApps);
        model.addAttribute("recentApps", recentApps);
        model.addAttribute("cvFilename", extractFilename(profile.getCvurl()));

        return "candidate/dashboard";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User currentUser, Model model) {
        Condidateprofile profile = candidateProfileRepository.findById(currentUser.getId()).orElse(null);
        if (profile == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("profile", profile);
        model.addAttribute("cvFilename", extractFilename(profile.getCvurl()));

        return "candidate/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String bio,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            updateCandidateProfileRequest req = new updateCandidateProfileRequest();
            req.setFistname(firstname);
            req.setLastname(lastname);
            req.setPhone(phone);
            req.setBio(bio);
            candidateProfileService.updateprofile(req, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Votre profil a été mis à jour avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/profile/cv")
    public String uploadCv(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            cvService.uploadcv(file, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Votre CV a été téléversé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'envoi du CV : " + e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/profile/skills/add")
    public String addSkill(
            @RequestParam("name") String name,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (name != null && !name.trim().isEmpty()) {
                String normalized = name.trim().toLowerCase().replaceAll("\\s", " ")
                        .replaceAll("[éèê]", "e")
                        .replaceAll("[àáâ]", "a")
                        .replaceAll("[ôóò]", "o");
                Condidateprofile candidate = candidateProfileRepository.findById(currentUser.getId()).orElse(null);
                if (candidate != null) {
                    skill skillCurrent = skillRepository.findByName(normalized)
                            .orElseGet(() -> skillRepository.save(new skill(normalized)));
                    candidate.getSkills().add(skillCurrent);
                    candidateProfileRepository.save(candidate);
                    redirectAttributes.addFlashAttribute("successMessage", "Compétence \"" + name.trim() + "\" ajoutée.");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @GetMapping("/applications")
    public String myApplications(@AuthenticationPrincipal User currentUser, Model model) {
        List<application> applications = applicationRepository.findAllByCondidatId(currentUser.getId());
        model.addAttribute("applications", applications);
        return "candidate/applications";
    }

    @PostMapping("/applications/{id}/apply")
    public String applyToJob(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            applicationService.addApplication(currentUser, id);
            redirectAttributes.addFlashAttribute("successMessage", "Votre candidature a été soumise avec succès ! Le recruteur a été notifié.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/jobs/" + id;
    }

    @PostMapping("/applications/{id}/cancel")
    public String cancelApplication(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            applicationService.annulerApp(currentUser, id);
            redirectAttributes.addFlashAttribute("successMessage", "Votre candidature a été retirée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible d'annuler la candidature : " + e.getMessage());
        }
        return "redirect:/candidate/applications";
    }
}
