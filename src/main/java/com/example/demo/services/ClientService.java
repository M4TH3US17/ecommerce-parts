package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Client;
import com.example.demo.repositories.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	public List<Client> findAll() {
		return repository.findAll();
	}

	public Client findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		return obj.get();
	}

	public Client save(Client obj) {
		return repository.save(obj);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	public Client update(Long id, Client obj) {
		Client entity = repository.getById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Client entity, Client obj) {
		entity.setName(obj.getName());
		entity.setContact(obj.getContact());
		entity.getAccount().setEmail(obj.getAccount().getEmail());
		entity.getAccount().setPassword(obj.getAccount().getPassword());

	}
}
