package com.example.demo.configurations.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.services.impl.ImplementationUserDetails;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementationUserDetails userDetailsService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
		.antMatchers(HttpMethod.POST, "/api/clients/register").permitAll()
		.antMatchers(HttpMethod.POST, "/api/clients/admin/register").hasRole("ADMIN")

		.antMatchers("/api/categories/**").hasRole("ADMIN")
		
		.antMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
		
		.antMatchers(HttpMethod.DELETE, "/api/clients/**").hasRole("USER")
		.antMatchers(HttpMethod.PUT, "/api/clients/**").hasRole("USER")
		.antMatchers(HttpMethod.GET, "/api/clients/**").hasRole("USER")
		.and().httpBasic();
		
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
