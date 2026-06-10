package com.saberpro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    
    @GetMapping("/")
    public String login() {
        return "login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/dashboard-redirect")
    public String redirectAfterLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        
        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (role.equals("ROLE_COORDINADOR")) {
            return "redirect:/coordinador/dashboard";
        } else if (role.equals("ROLE_DOCENTE")) {
            return "redirect:/docente/dashboard";
        } else {
            return "redirect:/estudiante/dashboard";
        }
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}