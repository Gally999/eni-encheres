
package fr.eni.ecole.projet.encheres.configuration.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class EncheresSecurityConfig {

	@Bean
	UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
		userDetailsManager.setUsersByUsernameQuery("SELECT pseudo, mot_de_passe, 1 FROM Utilisateurs WHERE pseudo = ?;");
		userDetailsManager.setAuthoritiesByUsernameQuery("SELECT pseudo, role FROM Utilisateurs u INNER JOIN Roles r ON u.administrateur = r.is_admin WHERE pseudo = ?;");
		return userDetailsManager;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> {

			auth
					// permettre à tout le monde d'accéder à l'URL racine


					.requestMatchers("/*").permitAll()
					.requestMatchers(HttpMethod.GET, "/article/creer").authenticated()
					.requestMatchers(HttpMethod.POST, "/article/creer").authenticated()
					.requestMatchers("/*").permitAll()

					// Permettre à tous les utilisateurs d'afficher correctement les images et la css
					.requestMatchers("/css/*").permitAll()
					.requestMatchers("/images/*").permitAll()

					// Toutes autres url et méthodes HTTP ne sont pas permises
					.anyRequest().denyAll();
		});

		// Customiser le formulaire de login
		http.formLogin(form -> {
			form.loginPage("/login").permitAll();
			form.defaultSuccessUrl("/session");
		});
		
		// Déconnexion
		http.logout(logout -> 
			logout
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.permitAll()
		);

		return http.build();
	}

}
