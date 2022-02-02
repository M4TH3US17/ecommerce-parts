package com.example.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Client;
import com.example.demo.repositories.ClientRepository;

@Service
public class ClientDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Qualifier("passwordCode")
	private PasswordEncoder encoder;

	@Autowired
	private ClientRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Client client = repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found in database!"));
		String[] roles;
		if(client.getAccount().isAdmin() == true) {
			roles = new String[] {"ADMIN", "USER"};
		} else {
			roles = new String[] {"USER"};
		}
		return User
				.builder()
				.username(client.getAccount().getEmail())
				.password(client.getAccount().getPassword())
				.roles(roles)
				.build();
	}
}
