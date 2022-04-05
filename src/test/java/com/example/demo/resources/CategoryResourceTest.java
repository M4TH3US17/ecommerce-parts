package com.example.demo.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
import com.example.demo.services.CategoryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryResourceTest {
	private static final Long ID_CATEGORY_INVALID = 2L;

	private static final Long ID_CATEGORY = 1L;
	private static final String NAME_CATEGORY = "Acessórios";
	
	@InjectMocks
	private CategoryResource resource;
	@Mock
	private CategoryService service;
	
	private Category category;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this.resource);
		startCategory();
	}
	
	@Test
	public void whenFindByIdThenReturnSuccess() throws Exception {
		when(service.findById(anyLong())).thenReturn(category);
		
		ResponseEntity<Category> response = resource.findById(ID_CATEGORY);
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		
		assertEquals(ID_CATEGORY, response.getBody().getId());
		assertEquals(NAME_CATEGORY, response.getBody().getName());
	}
	
	@Test
	private void whenSavingCategoryThenReturnCreated() {
		when(service.save(any())).thenReturn(category);
		
		category.setId(null);
		ResponseEntity<Category> response = resource.save(category);
		
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		
		assertEquals(ID_CATEGORY, response.getBody().getId());
		assertEquals(NAME_CATEGORY, response.getBody().getName());
	}

	private void startCategory() {
		category = new Category(ID_CATEGORY, NAME_CATEGORY);
	}
	
}
