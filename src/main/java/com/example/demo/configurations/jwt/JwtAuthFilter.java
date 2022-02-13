package com.example.demo.configurations.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.services.impl.ImplementationUserDetails;

public class JwtAuthFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private ImplementationUserDetails clientService;
	
	public JwtAuthFilter(JwtService jwtService, ImplementationUserDetails clientService) {
		this.jwtService = jwtService;
		this.clientService = clientService;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String authorization = request.getHeader("Authorization"); 
		
		if(authorization != null && authorization.startsWith("Bearer")) { /* se authorization ñ for nulo e começar com prefixo "Bearer" (true) */
			String token = authorization.substring(7); // captura o token
			boolean isValid = jwtService.validToken(token); // verifica se o token está válido
			
			if(isValid == true) {
				String loginClient = jwtService.captureEmailClient(token); // pego o username (email) do cliente
				UserDetails client = clientService.loadUserByUsername(loginClient); // carrego cliente pelo email
				
				// colocar client no contexto do spring security:
				UsernamePasswordAuthenticationToken obj = new UsernamePasswordAuthenticationToken(client, null, client.getAuthorities());
				obj.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(obj);
			}
		}
		filterChain.doFilter(request, response);
	}

}
