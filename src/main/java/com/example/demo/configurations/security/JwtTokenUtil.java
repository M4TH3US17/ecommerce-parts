package com.example.demo.configurations.security;

import java.io.Serializable;
import java.util.function.Function;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Configuration
public class JwtTokenUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	// chave de assinatura
	private static final String JWT_SECRET = "T2zDoSBNdW5kbw==";
	
	// expiração do token
	private static final Long EXPIRATION_TOKEN = 111111L;
	
	
	public String generationToken(UserDetailsService user) {
		return Jwts.builder()
				//.setSubject()
				.compact();
	}
	
	// retorna o username do token
	public String getUsernameFromToken(String token) {
		return null;
	}
	
	public <T> T TgetAllClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
		return  null;
	}
	
}
