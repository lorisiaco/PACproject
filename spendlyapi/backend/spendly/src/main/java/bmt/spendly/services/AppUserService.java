package bmt.spendly.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bmt.spendly.models.AppUser;
import bmt.spendly.repositories.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {
	
	@Autowired
	private AppUserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = repo.findByUsernameIgnoreCase(username);
		
		if (appUser != null) {
			var springUser = User.withUsername(appUser.getUsername())
						        .password(appUser.getPassword())
						        .roles(appUser.getRole())
						        .build();
			
			return springUser;
		}
		
		return null;
	}

}
