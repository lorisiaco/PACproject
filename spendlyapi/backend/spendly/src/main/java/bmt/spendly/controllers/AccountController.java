package bmt.spendly.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import bmt.spendly.models.AppUser;
import bmt.spendly.models.LoginDto;
import bmt.spendly.models.RegisterDto;
import bmt.spendly.repositories.AppUserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Value("${security.jwt.secret-key}")
    private String jwtSecretKey;
	
	@Value("${security.jwt.issuer}")
    private String jwtIssuer;
	
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	
	@PreAuthorize("hasRole('admin')")
	@GetMapping("/admin")
    public String helloAdmin(Authentication auth) {
    	return "Hello Admin: " + auth.getName();
    }
	
	@PreAuthorize("hasRole('client')")
	@GetMapping("/client")
    public String helloClient(Authentication auth) {
    	return "Hello Client: " + auth.getName();
    }
	
	
	@GetMapping("/profile")
    public ResponseEntity<Object>  profile(Authentication auth) {
		var response = new HashMap<String, Object>();
    	response.put("Username", auth.getName());
    	response.put("Authorities", auth.getAuthorities());
    	
    	var appUser = appUserRepository.findByUsernameIgnoreCase(auth.getName());
    	response.put("User", appUser);
    	
		return ResponseEntity.ok(response);
    }
	
	
	@PostMapping("/register")
    public ResponseEntity<Object> register(
    		@Valid @RequestBody RegisterDto registerDto
    		, BindingResult result) {
		
		
		if (result.hasErrors()) {
 		   var errorsList = result.getAllErrors();
 		   var errorsMap = new HashMap<String, String>();
 			
 		   for(int i = 0; i < errorsList.size(); i++) {
 			var error = (FieldError) errorsList.get(i);
 			errorsMap.put(error.getField(), error.getDefaultMessage());
 		   }
 			
 		   return ResponseEntity.badRequest().body(errorsMap);
		}
		
		
		var bCryptEncoder = new BCryptPasswordEncoder();
    	
        AppUser appUser = new AppUser();
        appUser.setFirstName(registerDto.getFirstName());
        appUser.setLastName(registerDto.getLastName());
        appUser.setUsername(registerDto.getUsername());
        appUser.setEmail(registerDto.getEmail());
        appUser.setRole("client");
        appUser.setCreatedAt(new Date());
        appUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
        
        
        
        try {
        	// check if username/email are used or not
        	var otherUser = appUserRepository.findByUsernameIgnoreCase(registerDto.getUsername());
        	if (otherUser != null) {
        		return ResponseEntity.badRequest().body("Username already used");
        	}
        	
        	otherUser = appUserRepository.findByEmail(registerDto.getEmail());
        	if (otherUser != null) {
        		return ResponseEntity.badRequest().body("Email address already used");
        	}
        	
        	appUserRepository.save(appUser);
        	
        	String jwtToken = createJwtToken(appUser);
        	
        	var response = new HashMap<String, Object>();
        	response.put("token", jwtToken);
        	response.put("user", appUser);

            return ResponseEntity.ok(response);
        }
        catch (Exception ex) {
        	System.out.println("There is an Exception :");
        	ex.printStackTrace();
        }
        
        
        return ResponseEntity.badRequest().body("Error");
		
	}
	
	
	@PostMapping("/login")
    public ResponseEntity<Object> login(
    		@Valid @RequestBody LoginDto loginDto
    		, BindingResult result) {
		
		if (result.hasErrors()) {
 		   var errorsList = result.getAllErrors();
 		   var errorsMap = new HashMap<String, String>();
 			
 		   for(int i = 0; i < errorsList.size(); i++) {
 			var error = (FieldError) errorsList.get(i);
 			errorsMap.put(error.getField(), error.getDefaultMessage());
 		   }
 			
 		   return ResponseEntity.badRequest().body(errorsMap);
		}
		
		
		try {
        	authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		loginDto.getUsername(),
                    		loginDto.getPassword()
                    )
            );
        	
        	AppUser appUser = appUserRepository.findByUsernameIgnoreCase(loginDto.getUsername());
        	
        	String jwtToken = createJwtToken(appUser);
        	
        	var response = new HashMap<String, Object>();
        	response.put("token", jwtToken);
        	response.put("user", appUser);

            return ResponseEntity.ok(response);
        }
        catch (Exception ex) {
        	System.out.println("There is an Exception :");
        	ex.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Bad username or password");
	}
	
	
	
	private String createJwtToken(AppUser appUser) {
		Instant now = Instant.now();
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer(jwtIssuer)
				.issuedAt(now)
				.expiresAt(now.plusSeconds(24 * 3600))
				.subject(appUser.getUsername())
				.claim("role", appUser.getRole())
				.build();
		
		var encoder = new NimbusJwtEncoder(
				new ImmutableSecret<>(jwtSecretKey.getBytes()));
		var params = JwtEncoderParameters.from(
				JwsHeader.with(MacAlgorithm.HS256).build(), claims);
				
		return encoder.encode(params).getTokenValue();
	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		// Recupera tutti gli utenti
		List<AppUser> users = appUserRepository.findAll();
		
		// Se vuoi nascondere la password prima di restituirli, lo puoi fare qui
		// ad esempio, impostandola a null
		users.forEach(u -> u.setPassword(null));
		
		return ResponseEntity.ok(users);
	}
}


































