package com.example.demo.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.services.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductResourceTest {
    private final static Long ID_PRODUCT_INVALID = 2L;
	
	private final static Long ID_PRODUCT = 1L;
	private final static String NAME_PRODUCT = "pneu";
	private final static String URL = "https://imagem.com";
	private final static Double PRICE= 20.00;
	private final static String DESCRIPTION = "serve pra fazer o carro se locomover";
	
	private final static Long ID_CATEGORY = 1L;
	private final static String NAME_CATEGORY = "Peças";
	
	private Product product;
	private List<Product> products;
	private Category category;
	
	@InjectMocks
	private ProductResource resource;
	@Mock
	private ProductService service;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this.resource);
		startProduct();
	}
	
	@Test
	public void whenFindByIdThenReturnSuccess() throws Exception {
		when(service.findById(anyLong())).thenReturn(product);
		
		ResponseEntity<Product> response = resource.findById(ID_CATEGORY);
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		
		assertEquals(ID_PRODUCT, response.getBody().getId());
		assertEquals(NAME_PRODUCT, response.getBody().getName());
		assertEquals(PRICE, response.getBody().getPrice());
		assertEquals(ID_CATEGORY, response.getBody().getCategory().getId());
	}

	private void startProduct() {
		products = new ArrayList<>();
		category = new Category(ID_CATEGORY, NAME_CATEGORY);
		product = new Product(ID_PRODUCT, NAME_PRODUCT, URL, PRICE, DESCRIPTION, category);
		products.add(product);
	}
}
