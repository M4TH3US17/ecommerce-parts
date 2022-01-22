package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Category;
import com.example.demo.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public List<Category> findAll() {
		return repository.findAll();
	}

	public Category findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		return obj.get();
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
	public Category update(Long id, Category obj) {
		Category entity = repository.getById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Category entity, Category obj) {
		entity.setName(obj.getName());

	}
}
