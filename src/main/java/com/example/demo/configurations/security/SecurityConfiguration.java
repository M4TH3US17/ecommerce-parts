package com.example.demo.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests().antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
		.antMatchers(HttpMethod.POST, "/api/clients/**").permitAll()
		
		.antMatchers("/api/categories/**").hasRole("ADMIN")
		
		.antMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
		
		.antMatchers("/api/clients/**").hasAnyRole("ADMIN", "USER")
		.and().formLogin().and().httpBasic();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.inMemoryAuthentication().withUser("matheus1").password(encoder().encode("matheus123")).roles("USER")
		.and()
		.withUser("matheus2").password(encoder().encode("matheus123")).roles("ADMIN", "USER");
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
