package com.abdo.JobManagement.webControllers;

import com.abdo.JobManagement.entities.user.Admin;
import com.abdo.JobManagement.entities.user.Condidateprofile;
import com.abdo.JobManagement.entities.user.Recruiter;
import com.abdo.JobManagement.entities.user.User;
import com.abdo.JobManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "CONDIDATE") String role,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("errorMessage", "Un compte avec cette adresse email existe déjà.");
            return "auth/register";
        }

        User user;
        if ("RECRUITER".equalsIgnoreCase(role)) {
            user = new Recruiter();
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            user = new Admin();
        } else {
            user = new Condidateprofile();
        }

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Votre compte a été créé avec succès ! Connectez-vous maintenant.");
        return "redirect:/login?registered=true";
    }

    @GetMapping("/dashboard")
    public String redirectDashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isRecruiter = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));
        boolean isCandidate = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CONDIDATE") || a.getAuthority().equals("ROLE_CANDIDATE"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else if (isRecruiter) {
            return "redirect:/recruiter/dashboard";
        } else if (isCandidate) {
            return "redirect:/candidate/dashboard";
        }

        return "redirect:/jobs";
    }
}
