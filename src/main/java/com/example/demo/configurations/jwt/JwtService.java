package com.example.demo.configurations.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Client;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	// Expiração (1 dia)
	private final long expiration = 86400000;

	// Chave de Assinatura
	private final String signature = "TWF0aGV1cyDDqSBsaW5kbw==";

	// Método que gera um token a partir das informações do cliente
	public String generationToken(Client client) {
		return Jwts.builder()
				// Email é único no database
				.setSubject(client.getEmail())
				.claim("name", client.getName())
				.claim("contact", client.getContact())
				.claim("password", client.getPassword())
				// Gera uma data de expiração pro Token
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.claim("roles", client.getAuthorities())
				// Gera um algoritmo de encode e uma chave
				.signWith(SignatureAlgorithm.HS512, signature).compact();
	}
	
	// Método que captura as informações do Token
	public Claims captureToken(String token) throws ExpiredJwtException {
		return Jwts
				.parser() // método que vai decodificar o token (fazer um parser)
				.setSigningKey(signature) // passo a chave
				.parseClaimsJws(token) // pega os claims do token
				.getBody(); // retorna as informações do token
	}
	
	// Método responsável por verificar se o token está válido ou não
	public boolean validToken(String token) {
		try {
			Claims claims = captureToken(token); // retorna as claims do Token			
			LocalDateTime time = claims.getExpiration().toInstant() // pega a data/hora atual
					.atZone(ZoneId.systemDefault()).toLocalDateTime();
			
			return !LocalDateTime.now().isAfter(time); // Token não será válido se a data/hora atual for depois de 
		// se lançar qualquer erro o Token está inválido	
		} catch (Exception e) {
			return false;
		}
	}
	
	// Método q irá dizer quem é o cliente q mandou o Token
	public String captureEmailClient(String email) throws ExpiredJwtException {
		return (String) captureToken(email).getSubject();
	}

	/*public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EcommercePartsApplication.class);

		JwtService service = context.getBean(JwtService.class);
		Client client = new Client(4L, "fulano", "fulano@gmail.com", "123", "(00) 00000-0000");
		client.getRoles().add(new Role(null, "ROLE_USER"));
		String token = service.generationToken(client);
		System.out.println(token);
		
		boolean isTokenValid = service.validToken(token);
		System.out.println("Token é válido? " + isTokenValid);
		System.out.println(service.captureEmailClient(token));
	}*/
}
