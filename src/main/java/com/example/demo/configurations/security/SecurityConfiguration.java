package com.example.demo.configurations.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.configurations.jwt.JwtAuthFilter;
import com.example.demo.configurations.jwt.JwtService;
import com.example.demo.services.impl.ImplementationUserDetails;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementationUserDetails userDetailsService;
	@Autowired
	private JwtService jwtService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()     
		   .antMatchers(HttpMethod.GET, "/api/products/**")
		       .permitAll()
		       
		   .antMatchers(HttpMethod.POST, "/api/clients/register")
		       .permitAll()
		   .antMatchers(HttpMethod.POST, "/api/clients/admin/register")
			   .hasRole("ADMIN")
		       
		   .antMatchers("/api/categories/**")
		       .hasRole("ADMIN")
		   .antMatchers(HttpMethod.POST, "/api/products")
		       .hasRole("ADMIN")
		   .antMatchers(HttpMethod.PUT, "/api/products/**")
		       .hasRole("ADMIN")
		   .antMatchers(HttpMethod.DELETE, "/api/products/**")
		       .hasRole("ADMIN")
		       
		   .antMatchers(HttpMethod.DELETE, "/api/clients/**")
		       .hasRole("USER")
		   .antMatchers(HttpMethod.PUT, "/api/clients/**")
		       .hasRole("USER")
		   .antMatchers(HttpMethod.GET, "/api/clients/**")
		       .hasRole("USER")
		.and()
		    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/v2/api-docs",
				"/configuration/ui", 
				"/swagger-resources/**",
				"/configuration/security", 
				"/swagger-ui.html",
				"/webjars/**",
				"/h2-console/**");
	}
	
	@Bean
	public OncePerRequestFilter jwtFilter() {
		return new JwtAuthFilter(jwtService, userDetailsService);
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
