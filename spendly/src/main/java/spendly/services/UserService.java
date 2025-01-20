package spendly.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.var;
import spendly.models.UserApp;
import spendly.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
            UserApp user= repo.findByEmail(email);

            if(user != null){
                var springUser= User.withUsername(user.getEmail())
                .password(user.getPassword())
                .build();

                return springUser;
            }
            return null;
        }
}
