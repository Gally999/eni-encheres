package fr.eni.ecole.projet.encheres.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class EncheresSecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> {
			
			auth	
				//permettre à tout le monde d'accéder à l'URL racine
				.requestMatchers("/*").permitAll()
				
				// Permettre à tous les utilisateurs d'afficher correctement les images et la css
				.requestMatchers("/css/*").permitAll()
				.requestMatchers("/images/*").permitAll()
				
				// Toutes autres url et méthodes HTTP ne sont pas permises
				.anyRequest().denyAll();
		});
		
	    // Customiser le formulaire de login
		http.formLogin(Customizer.withDefaults());
		
		return http.build();
	}

}
