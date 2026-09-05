package com.abdo.JobManagement.webControllers;

import com.abdo.JobManagement.exceptions.RessourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackages = "com.abdo.JobManagement.webControllers")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebGlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied() {
        return "error/403";
    }

    @ExceptionHandler(RessourceNotFoundException.class)
    public String handleNotFound(RessourceNotFoundException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return referer != null ? "redirect:" + referer : "redirect:/jobs";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return referer != null ? "redirect:" + referer : "redirect:/jobs";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue : " + ex.getMessage());
        return referer != null ? "redirect:" + referer : "redirect:/jobs";
    }
}
