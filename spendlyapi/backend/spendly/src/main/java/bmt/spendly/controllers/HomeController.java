package bmt.spendly.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController implements HomeControllerIF {

    @Override
    @GetMapping("/")
    public String home() {
        return "index"; // Senza redirect, carica direttamente la vista
    }

    @Override
    @GetMapping("/store")
    public String store() {
        return "Store page";
    }

    @Override
    @GetMapping("/admin/home")
    public String getAdminHome() {
        return "Admin Home Page";
    }

    @Override
    @GetMapping("/client/home")
    public String getClientHome() {
        return "Client Home Page";
    }
}
