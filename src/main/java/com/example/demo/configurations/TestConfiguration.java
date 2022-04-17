package com.example.demo.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Category;
import com.example.demo.entities.Client;
import com.example.demo.entities.Product;
import com.example.demo.entities.Role;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ClientRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.RoleRepository;

@Configuration
@Profile("test")
public class TestConfiguration implements CommandLineRunner {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		Role r1 = new Role(null, "ROLE_ADMIN");
		Role r2 = new Role(null, "ROLE_USER");
		roleRepository.saveAll(Arrays.asList(r1, r2));

		Client c1 = new Client(null, "Administrador", "administrador@gmail.com", encoder.encode("admin123"),
				"(92) 92702070");
		c1.getRoles().add(r1);
		c1.getRoles().add(r2);
		Client c2 = new Client(null, "Usuario", "usuario@gmail.com", encoder.encode("usuario123"), "(11) 91777-7777");
		c2.getRoles().add(r2);
		clientRepository.saveAll(Arrays.asList(c1, c2));

		Category cat1 = new Category(null, "Peças");
		Category cat2 = new Category(null, "Acessórios");
		categoryRepository.saveAll(Arrays.asList(cat1, cat2));

		Product p1 = new Product(null, "pneu", "https://image/pneu", 50.00, "serve pra fzr o carro caminhar", cat1);
		Product p2 = new Product(null, "vidro", "https://image/vidro", 400.00, "vidro frontal do carro", cat1);
		Product p3 = new Product(null, "calha de chuva", "https://image/calha", 30.00,
				"serve pra sla oq, mas é acessório", cat2);
		Product p4 = new Product(null, "parafuso pneu", "https://image/parafuso", 30.00, "serve pra parafusar", cat1);
		productRepository.saveAll(Arrays.asList(p1, p2, p3, p4));
	}
}
