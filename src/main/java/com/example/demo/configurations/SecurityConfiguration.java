package com.example.demo.configurations;

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
	
	@Override // realiza a autorização
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/products").permitAll()
		.antMatchers("/categories/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN") 
		.antMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
		.antMatchers("/clients/**").hasRole("USER")
		.and().formLogin();
		
		//http.httpBasic();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.passwordEncoder(passwordEncoder()) // chamo o método passwordEncoder() pra encriptar
		.withUser("matheus")
		.password(passwordEncoder().encode("matheus123")) //chamo o método passwordEncorder() pra encriptar a senha
		.roles("USER")
		.and()
		.passwordEncoder(passwordEncoder()).withUser("matheus2").password(passwordEncoder().encode("matheus123"))
		.roles("USER","ADMIN");
	}
	
	@Bean // Responsável por criptografar, gerar um hash, para cada senha
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}
}
