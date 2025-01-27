package com.bmt.spendly.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"", "/"})
    public String home() {
        return "index";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Dashboard"; // Questo carica templates/Dashboard.html
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/gestione-spese")
    public String gestioneSpese() {
        return "gestione-spese";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/gestione-gruppi")
    public String gestioneGruppi() {
        return "gestione-gruppi";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/gestione-spese-gruppo")
    public String gestioneSpeseGruppo() {
        return "gestione-spese-gruppo";
    }
}





















