package com.bmt.spendly.controllers;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.bmt.spendly.models.AppUser;
import com.bmt.spendly.models.RegisterDto;
import com.bmt.spendly.repositories.AppUserRepository;

import jakarta.validation.Valid;

@Controller
public class AccountController {

	@Autowired
	private AppUserRepository repo;

	@GetMapping("/profile")
	public String profile(Authentication auth, Model model) {
		AppUser user = repo.findByEmail(auth.getName());
		model.addAttribute("appUser", user);
		
		// Carichiamo una view dedicata al profilo (che creeremo sotto)
		return "profile"; 
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		RegisterDto registerDto = new RegisterDto();
		model.addAttribute(registerDto);
		model.addAttribute("success", false);
		return "register";
	}

	@PostMapping("/register")
	public String register(Model model,
						@Valid @ModelAttribute("registerDto") RegisterDto registerDto,
						BindingResult result) 
	{
		if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
			result.addError(new FieldError("registerDto", "confirmPassword", "Password mismatch"));
		}

		AppUser appUserCheck = repo.findByEmail(registerDto.getEmail());
		if (appUserCheck != null) {
			result.addError(new FieldError("registerDto", "email", "Email already used"));
		}

		if (result.hasErrors()) {
			return "register";
		}

		try {
			var bCryptEncoder = new BCryptPasswordEncoder();

			AppUser newUser = new AppUser();
			newUser.setFirstName(registerDto.getFirstName());
			newUser.setLastName(registerDto.getLastName());
			newUser.setEmail(registerDto.getEmail());
			newUser.setPhone(registerDto.getPhone());
			newUser.setAddress(registerDto.getAddress());
			newUser.setRole("client");
			newUser.setCreatedAt(new Date());
			newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));

			// Gestione upload
			if (registerDto.getProfileImage() != null && !registerDto.getProfileImage().isEmpty()) {
				// Salviamo in una cartella esterna
				String originalFilename = registerDto.getProfileImage().getOriginalFilename();
				String extension = "";
				if (originalFilename != null && originalFilename.contains(".")) {
					extension = originalFilename.substring(originalFilename.lastIndexOf("."));
				}

				String generatedFilename = UUID.randomUUID().toString() + extension;

				// Cartella di destinazione
				Path uploadPath = Paths.get("C:/SpendlyUploads");
				Files.createDirectories(uploadPath); // Crea se non esiste

				// Copia su disco
				Path filePath = uploadPath.resolve(generatedFilename);
				registerDto.getProfileImage().transferTo(filePath);

				// Salviamo nel DB il path accessibile come risorsa
				// Se hai configurato l'handler statico: "/images/uploads/**" -> "file:///C:/SpendlyUploads/"
				newUser.setProfileImage("/images/uploads/" + generatedFilename);
			} else {
				// Immagine di default
				newUser.setProfileImage("/images/default.webp");
			}

			repo.save(newUser);

			model.addAttribute("registerDto", new RegisterDto());
			model.addAttribute("success", true);
		}
		catch (Exception ex) {
			result.addError(new FieldError("registerDto", "firstName", ex.getMessage()));
		}

		return "register";
	}
}




















