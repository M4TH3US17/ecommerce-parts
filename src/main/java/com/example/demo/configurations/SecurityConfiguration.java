package com.example.demo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.services.impl.ClientDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ClientDetailsServiceImpl clientServiceDetailsImpl;

	@Override // realiza a autorização da URL
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.GET, "/products").permitAll()
		.antMatchers(HttpMethod.POST, "/clients").permitAll()

		.antMatchers("/categories/**").hasRole("ADMIN")

	    .antMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST, "/products").hasRole("ADMIN") 
		.antMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

		.antMatchers("/clients/**").hasAnyRole("USER", "ADMIN")
		.and().formLogin().and().httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(clientServiceDetailsImpl)
		.passwordEncoder(passwordCode());
	}

	@Bean // Responsável por criptografar, gerar um hash, para cada senha
	public PasswordEncoder passwordCode() {
		return new BCryptPasswordEncoder();
	}
}
