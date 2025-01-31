package com.bookstore.BookStore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

	@GetMapping("/")
    public String home() {
        return "index"; // Senza redirect, carica direttamente la vista
    }
	
	@GetMapping("/store")
    public String store() {
    	return "Store page";
    }
	
	@GetMapping("/admin/home")
	public String getAdminHome() {
		return "Admin Home Page";
	}
	
	@GetMapping("/client/home")
	public String getClientHome() {
		return "Client Home Page";
	}
}
