package com.example.demo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Client;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.services.exceptions.notfound.ClientNotFoundException;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	
	/*public List<Client> findAll() {
		return repository.findAll();
	}*/

	public Client findById(Long id) throws ClientNotFoundException {
		Client obj = repository.findById(id).orElseThrow(
				() ->  new ClientNotFoundException("Client with id " + id + " not found."));
		return obj;
	}
	
	public Page<Client> findByPage(Pageable pageable){
		return repository.findAll(pageable);
	}

	@Transactional
	public Client save(Client obj) {
		return repository.save(obj);
	}

	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Modifying
	@Transactional
	public Client update(Long id, Client obj) throws ProductNotFoundException {
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
		entity.getAccount().setEmail(obj.getAccount().getEmail());
		entity.getAccount().setPassword(obj.getAccount().getPassword());

	}
}
