package org.ticketplatform.java.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@SuppressWarnings("removal")

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests()
		.requestMatchers("/tickets/create").hasAuthority("ADMIN")
		.requestMatchers("/", "/tickets", "/tickets/show/*", "/tickets/edit/*", "/notes/show", "notes/edit/*", "/users/show").hasAnyAuthority("ADMIN", "OPERATOR")
		.requestMatchers(HttpMethod.POST, "/tickets/create").hasAnyAuthority("ADMIN")
		.requestMatchers(HttpMethod.POST, "/tickets/edit/*", "/notes/edit/*", "/notes/create").hasAnyAuthority("ADMIN", "OPERATOR")
		.requestMatchers("/**").permitAll()
		.and().formLogin().and().logout()
		.and().exceptionHandling()
		.and().csrf().disable();

		return http.build();
	}

	@Bean
	DatabaseUserDetailsService userDetailsService() {
		return new DatabaseUserDetailsService();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

}