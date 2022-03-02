package com.example.demo.services;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
	//Product with id 2 was not mocked, that is, it does not exist.
	private final static Long ID_PRODUCT_INVALID = 2L;
	
	private final static Long ID_PRODUCT = 1L;
	private final static String NAME_PRODUCT = "pneu";
	private final static String URL = "https://imagem.com";
	private final static Double PRICE= 20.00;
	private final static String DESCRIPTION = "serve pra fazer o carro se locomover";
	
	private final static Long ID_CATEGORY = 1L;
	private final static String NAME_CATEGORY = "Peças";
	
	@InjectMocks
	private ProductService service;
	@Mock
	private ProductRepository repository;
	
	private Product product;
	private Category category;
	private Optional<Product> optionalProduct;
	private List<Product> products;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this.service);
		startProduct();
	}

	@Test
	public void whenFindByIdThenReturnAnProductInstance() throws ProductNotFoundException {
		Mockito
		 .when(repository.findById(Mockito.anyLong()))
		 .thenReturn(optionalProduct);
		
	   Product response = service.findById(ID_PRODUCT);
	   
	   assertNotNull(response);
	   assertEquals(Product.class, response.getClass());
	   assertEquals(ID_PRODUCT, response.getId());
	   assertEquals(NAME_PRODUCT, response.getName());
	   assertEquals(ID_CATEGORY, response.getCategory().getId());
	   assertEquals(NAME_CATEGORY, response.getCategory().getName());
	}
	
	@Test
	public void whenFindByIdThenReturnAnProductNotFoundException() {
		Mockito
		 .when(repository.findById(ID_PRODUCT))
		 .thenReturn(optionalProduct);
		
		var thrown = assertThrows(ProductNotFoundException.class,
                                  () -> { 
                                	  service.findById(ID_PRODUCT_INVALID); 
                                  },
                                  "Failed: \"whenFindByIdThenReturnAnProductNotFoundException\" test failed.");
		
		assertThat(thrown).hasMessage("Product with id "+ID_PRODUCT_INVALID+" not found.");
		verify(repository, Mockito.times(1)).findById(ID_PRODUCT_INVALID);
	}
	
	@Test
	public void whenFindProductByCategoryThenReturnAnProductInstance() throws ProductNotFoundException {
		Mockito
		  .when(repository.findProductsByCategory(NAME_CATEGORY)).thenReturn(products);
		
		List<Product> list = service.findProductByCategory(NAME_CATEGORY);
		
		assertEquals(category.getName(), list.get(0).getCategory().getName());
	}
	
	@Test
	public void whenSavingProductReturnSuccess() {
		Mockito
		  .when(repository.save(Mockito.any())).thenReturn(product);
		
		product.setId(2L);
		product.setCategory(category);
		Product response = service.save(product);
		
		assertNotNull(response);
		assertEquals(Product.class, response.getClass());
		assertEquals(2L, response.getId());
		assertEquals(NAME_PRODUCT, response.getName());
		assertEquals(NAME_CATEGORY, response.getCategory().getName());
	}
	
	@Test
	public void whenUpdateTheProductReturnSuccess() throws ProductNotFoundException {
		Mockito.when(repository.existsById(ID_PRODUCT)).thenReturn(true);
		Mockito.when(repository.getById(ID_PRODUCT)).thenReturn(product);
		Mockito.when(repository.save(Mockito.any())).thenReturn(product);
		
		product.setName("porta");
		product.setCategory(category);
		Product response = service.update(ID_PRODUCT, product);
		
		assertEquals("porta", response.getName());
		assertEquals(category.getId(), response.getCategory().getId());
		verify(repository, Mockito.times(1)).save(Mockito.any());
	}
	
	@Test
	public void whenUpdateTheProductReturnProductNotFoundException() throws ProductNotFoundException {
		Mockito.when(repository.existsById(ID_PRODUCT_INVALID)).thenReturn(false);
		Mockito.when(repository.getById(ID_PRODUCT)).thenReturn(product);
		
		var thrown = assertThrows(ProductNotFoundException.class,
				                  () -> {
				                	product.setName("porta");
				              		product.setCategory(category);
				              		service.update(ID_PRODUCT_INVALID, product);
				                  });
		assertThat(thrown).hasMessage("Product with id "+ID_PRODUCT_INVALID+" not found.");
		assertEquals(category.getId(), product.getCategory().getId());
		verify(repository, Mockito.times(0)).save(product);
		verify(repository, Mockito.times(1)).existsById(ID_PRODUCT_INVALID);
	}
	
	public void deleteById() {
	}
	
	private void startProduct() {
		products = new ArrayList<>();
		category = new Category(ID_CATEGORY, NAME_CATEGORY);
		product = new Product(ID_PRODUCT, NAME_PRODUCT, URL, PRICE, DESCRIPTION, category);
		products.add(product);
		
		optionalProduct = Optional.of(product);
	}
	
}
