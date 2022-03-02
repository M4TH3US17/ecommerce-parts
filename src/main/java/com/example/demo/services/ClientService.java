package com.example.demo.services;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Client;
import com.example.demo.entities.Role;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.resources.exceptions.verification.PasswordInvalidException;
import com.example.demo.services.exceptions.notfound.ClientNotFoundException;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;
import com.example.demo.services.impl.ImplementationUserDetails;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ImplementationUserDetails clientServiceImpl;
	@Autowired
	private PasswordEncoder encoder;

	public Client findById(Long id) throws ClientNotFoundException {
		Client obj = repository.findById(id).orElseThrow(
				() ->  new ClientNotFoundException("Client with id " + id + " not found."));
		return obj;
	}
	
	public Page<Client> findByPage(Pageable pageable){
		return repository.findAll(pageable);
	}

	@Transactional
	public Client saveClient(Client obj) {
		Client client = addRoleToClient(obj); // adiciona a ROLE_USER automaticamente
		return repository.save(client);
	}

	@Transactional
	public Client saveAdmin(Client obj) {
		Client client = addRoleToAdmin(obj); // adiciona a ROLE_USER e ROLE_ADMIN automaticamente
		return repository.save(client);
	}
	
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Modifying
	@Transactional
	public Client update(Long id, Client obj) throws ProductNotFoundException, NoSuchElementException {
		if(repository.existsById(id) == false) {
			throw new ProductNotFoundException("Client with id "+id+" not found.");
		}
		Client entity = repository.getById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}
	
	private void updateData(Client entity, Client obj) {
		entity.setName(obj.getName());
		entity.setContact(obj.getContact());
		entity.setEmail(obj.getEmail());
		entity.setPassword(encoder.encode(obj.getPassword()));
	}
	
	private Client addRoleToClient(Client obj) {
		Set<Role> role = new HashSet<>();
		role.add(roleRepository.findByName("ROLE_USER"));
		obj.setRoles(role);
		return obj;
	}
	
	private Client addRoleToAdmin(Client obj) {
		Set<Role> role = new HashSet<>();
		role.add(roleRepository.findByName("ROLE_ADMIN"));
		role.add(roleRepository.findByName("ROLE_USER"));
		obj.setRoles(role);
		return obj;
	}
	
	// verifica se a senha do banco é a mesma que a senha informada
		public Client authenticate(Client obj) throws PasswordInvalidException { 
			UserDetails client = clientServiceImpl.loadUserByUsername(obj.getEmail());
			// compara as senhas (senha do banco e senha informada)
			boolean validationPassword = encoder.matches(obj.getPassword(), client.getPassword()); 
			if(validationPassword) {
				Client object = repository.findByEmail(obj.getEmail()).orElseThrow();
				return object;
			}
			throw new PasswordInvalidException("Password Invalid");
		}
}
