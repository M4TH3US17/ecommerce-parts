package com.example.demo.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Client;
import com.example.demo.services.ClientService;

@RestController
@RequestMapping(value = "/api/clients")
public class ClientResource {

	@Autowired
	private ClientService service;
	
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Client> findById(@PathVariable Long id) throws Exception {
		return ResponseEntity.ok().body(service.findById(id));
	}
	
	@GetMapping(value = "/pagination", produces = "application/json")
	public ResponseEntity<Page<Client>> findClientsByPage(@PageableDefault(direction = Sort.Direction.ASC, size = 5, sort = "id")Pageable pageable){
		return ResponseEntity.ok().body(service.findByPage(pageable));
	}
	
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Client> save(@Valid @RequestBody Client obj){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(obj));
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Client> update(@PathVariable Long id, @Valid @RequestBody Client obj) throws Exception {
		return ResponseEntity.ok().body(service.update(id, obj));
	}
}
