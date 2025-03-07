package bmt.spendly.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import bmt.spendly.services.AppUserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests( auth -> auth
                		.requestMatchers("/admin/**").hasAnyRole("admin")
                		.requestMatchers("/client/**").hasAnyRole("client")
	                    .requestMatchers("/seller/**").hasAnyRole("seller")
	                    .requestMatchers("/").permitAll()
						.requestMatchers("/api/**").permitAll()
						.requestMatchers("/api/groups/**").permitAll()
	                    .requestMatchers("/store/**").permitAll()
	                    .requestMatchers("/account").permitAll()
	                    .requestMatchers("/account/login").permitAll()
	                    .requestMatchers("/account/register").permitAll()
	                    .anyRequest().authenticated() // (2)
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(
                		SessionCreationPolicy.STATELESS))
                .build();
    }
	
	
	@Bean
	public JwtDecoder jwtDecoder() {
		var secretKey = new SecretKeySpec(jwtSecretKey.getBytes(), "");
		return NimbusJwtDecoder.withSecretKey(secretKey)
				.macAlgorithm(MacAlgorithm.HS256).build();
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AppUserService appUserService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(appUserService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		
		return new ProviderManager(provider);
	}
	
	
	@Bean
	public JwtAuthenticationConverter getConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}

