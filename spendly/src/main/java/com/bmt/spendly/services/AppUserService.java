package com.bmt.spendly.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bmt.spendly.models.AppUser;
import com.bmt.spendly.repositories.AppUserRepository;



@Service
public class AppUserService implements UserDetailsService {
	@Autowired
	private AppUserRepository repo;

	// Metodo per ottenere tutti gli utenti
    public List<AppUser> getAllUsers() {
        return repo.findAll();
    }
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser appUser = repo.findByEmail(email);
		
		
		if (appUser != null) {
			var springUser = User.withUsername(appUser.getEmail())
			        .password(appUser.getPassword())
			        .roles(appUser.getRole())
			        .build();

			return springUser;
		}
		
		
		return null;
	}

}
