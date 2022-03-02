package com.example.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

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
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.exceptions.notfound.CategoryNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
	//Category with id 2 was not mocked, that is, it does not exist.
	private static final Long ID_CATEGORY_INVALID = 2L;

	private static final Long ID_CATEGORY = 1L;
	private static final String NAME_CATEGORY = "Acessórios";
	
	@InjectMocks
	private CategoryService service;
	@Mock
	private CategoryRepository repository;
	
	private Category category;
	private Optional<Category> optionalCategory;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this.service);
		startCategory();
	}
	
	@Test
	public void whenFindByIdThenReturnAnCategoryInstance() throws CategoryNotFoundException {
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(optionalCategory);
		
		Category response = service.findById(ID_CATEGORY);
		
		assertNotNull(response);
		assertEquals(ID_CATEGORY, response.getId());
		assertEquals(NAME_CATEGORY, response.getName());
	}
	
	@Test
	public void whenFindByIdThenReturnAnCategoryNotFoundException() {
		Mockito.when(repository.findById(ID_CATEGORY)).thenReturn(optionalCategory);
		
		var thrown = assertThrows(CategoryNotFoundException.class, 
				                 () -> {
				                	 service.findById(ID_CATEGORY_INVALID);
				                 }, 
				                 "Failed: \"whenFindByIdThenReturnAnCategoryNotFoundException\" test failed.");
		assertThat(thrown).hasMessage("Category with id "+ID_CATEGORY_INVALID+" not found.");
		verify(repository, Mockito.times(1)).findById(ID_CATEGORY_INVALID);
	}
	
	@Test
	public void whenSavingTheCategoryReturnSuccess() {
		Mockito.when(repository.save(Mockito.any())).thenReturn(category);
		
		Category response = service.save(category);
		
		assertNotNull(response);
		assertEquals(ID_CATEGORY, response.getId());
		assertEquals(NAME_CATEGORY, response.getName());
		assertEquals(Category.class, response.getClass());
		verify(repository, Mockito.times(1)).save(response);
	}
	
	@Test
	public void whenUpdateTheCategoryReturnSuccess() throws CategoryNotFoundException {
		Mockito.when(repository.existsById(Mockito.anyLong())).thenReturn(true);
		Mockito.when(repository.getById(Mockito.anyLong())).thenReturn(category);
		Mockito.when(repository.save(Mockito.any())).thenReturn(category);
		
		category.setName("Acessórios");
		Category response = service.update(ID_CATEGORY, category);
		
		assertNotNull(response);
		assertEquals(ID_CATEGORY, response.getId());
		assertEquals(NAME_CATEGORY, response.getName());
		assertEquals(Category.class, response.getClass());
		verify(repository, Mockito.times(1)).save(response);
	}
	
	@Test
	public void whenUpdateTheCategoryReturnCategoryNotFoundException() throws CategoryNotFoundException {
		Mockito.when(repository.existsById(Mockito.anyLong())).thenReturn(false);
		Mockito.when(repository.getById(Mockito.anyLong())).thenReturn(category);
		Mockito.when(repository.save(Mockito.any())).thenReturn(category);
		
		var thrown = assertThrows(CategoryNotFoundException.class,
				() -> {
					category.setName("Acessórios");
					service.update(ID_CATEGORY_INVALID, category);
				});
		
		assertThat(thrown).hasMessage("Category with id "+ID_CATEGORY_INVALID+" not found.");
		verify(repository, Mockito.times(0)).save(Mockito.any());
		verify(repository, Mockito.times(1)).existsById(ID_CATEGORY_INVALID);
	}
	
	private void startCategory() {
		category = new Category(ID_CATEGORY, NAME_CATEGORY);
		optionalCategory = Optional.of(category);
	}
}
