package com.example.demo.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Account;
import com.example.demo.entities.Category;
import com.example.demo.entities.Client;
import com.example.demo.entities.Product;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.repositories.ProductRepository;

@Configuration
@Profile("test")
public class TestConfiguration implements CommandLineRunner {

	@Autowired 
	private ClientRepository clientRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	@Qualifier("passwordCode")
	private PasswordEncoder encoder;
	
	@Override
	public void run(String... args) throws Exception {
		Client c1 = new Client(null, "Matheus Dalvino", new Account("matheusdalvino50@gmail.com", encoder.encode("matheus123"), true /*ADMIN*/), "(92) 92702070");
	    Client c2 = new Client(null, "Pedro Almeida", new Account("pedro@gmail.com",  encoder.encode("pedro123"), false/*USER*/), "(11) 91777-7777");
		clientRepository.saveAll(Arrays.asList(c1, c2));
		
		Category cat1 = new Category(null, "Peças");
		Category cat2 = new Category(null, "Acessórios");
		categoryRepository.saveAll(Arrays.asList(cat1, cat2));
		
		Product p1 = new Product(null, "pneu","https://image/pneu", 50.00, "serve pra fzr o carro caminhar", cat1);
		Product p2 = new Product(null, "vidro","https://image/vidro", 400.00, "vidro frontal do carro", cat1);
		Product p3 = new Product(null, "calha de chuva","https://image/calha", 30.00, "serve pra sla oq, mas é acessório", cat2);
		Product p4 = new Product(null, "parafuso pneu","https://image/parafuso", 30.00, "serve pra parafuzar", cat1);
		productRepository.saveAll(Arrays.asList(p1,p2,p3,p4));
	}

}
