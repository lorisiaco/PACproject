package bmt.spendly.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import bmt.spendly.models.LoginDto;
import bmt.spendly.models.RegisterDto;

public interface AccountControllerIF {
    String helloAdmin(Authentication auth);
    
    String helloClient(Authentication auth);
    
    ResponseEntity<Object> profile(Authentication auth);
    
    ResponseEntity<Object> register(RegisterDto registerDto, BindingResult result);
    
    ResponseEntity<Object> login(LoginDto loginDto, BindingResult result);
    
    ResponseEntity<?> getAllUsers();
}
