package com.example.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Client;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.resources.exceptions.verification.PasswordInvalidException;

@Service
public class ImplementationUserDetails implements UserDetailsService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Client client = clientRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Email not found in database."));
		return new User(client.getUsername(), client.getPassword(), client.getRoles());
	}

	// verifica se a senha do banco é a mesma que a senha informada
	public UserDetails authenticate(Client obj) throws PasswordInvalidException { 
		UserDetails client = loadUserByUsername(obj.getEmail());
		// compara as senhas (senha do banco e senha informada)
		boolean validationPassword = encoder.matches(obj.getPassword(), client.getPassword()); 
		if(validationPassword) {
			return client;
		}
		throw new PasswordInvalidException("Password Invalid");
	}

}
