package com.example.demo.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Category;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.exceptions.notfound.CategoryNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public List<Category> findAll() {
		return repository.findAll();
	}

	public Category findById(Long id) throws CategoryNotFoundException {
		Category obj = repository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found."));
		return obj;
	}
	
	public Page<Category> findByPage(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Transactional
	public Category save(Category obj) {
		return repository.save(obj);
	}

	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Modifying
	@Transactional
	public Category update(Long id, Category obj) throws CategoryNotFoundException {
		if(repository.existsById(id) == false) {
			throw new CategoryNotFoundException("Category with id "+id+" not found.");
		}
		Category entity = repository.getById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Category entity, Category obj) {
		entity.setName(obj.getName());

	}
}
