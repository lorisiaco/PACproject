package spendly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityUser {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
            .authorizeHttpRequests( auth -> auth
            .requestMatchers("/").permitAll()       //tutte queste pagine non hanno bisogno di permesso per visualizzare
            .requestMatchers("/contact").permitAll()
            .requestMatchers("/store/**").permitAll()
            .requestMatchers("/register").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/logout").permitAll()
            .anyRequest().authenticated()       //Tutte le altre si
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/",true)    //con il successo di login verrai indirizzato a "/"
            )
            .logout(config -> config.logoutSuccessUrl("/"))  //con il successo di logout verrai indirizzato a "/"
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
