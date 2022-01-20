package com.example.demo.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.demo.entities.Account;
import com.example.demo.entities.Category;
import com.example.demo.entities.Client;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ClientRepository;

@Configuration
@Profile("test")
public class TestConfiguration implements CommandLineRunner {

	/* no perfil de teste, os dados mockados no
	   método run serão salvados automaticamente
	   pelo repository */
	@Autowired 
	private ClientRepository clientRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public void run(String... args) throws Exception {
		Client c1 = new Client(null, "Matheus Dalvino", new Account("matheusdalvino50@gmail.com", "123"), "(92) 92702070");
	    Client c2 = new Client(null, "Pedro Almeida", new Account("pedro@gmail.com", "123"), "(11) 91777-7777");
		clientRepository.saveAll(Arrays.asList(c1, c2));
		
		Category cat1 = new Category(null, "Peças");
		Category cat2 = new Category(null, "Acessórios");
		categoryRepository.saveAll(Arrays.asList(cat1, cat2));
	}

}
