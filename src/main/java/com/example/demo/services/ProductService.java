package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	public List<Product> findAll() {
		return repository.findAll();
	}

	public Product findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		return obj.get();
	}

	public List<Product> findProductByCategory(String category) {
		return repository.findProductsByCategory(category);
	}

	@Transactional
	public Product save(Product obj) {
		return repository.save(obj);
	}

	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Modifying
	@Transactional
	public Product update(Long id, Product obj) {
		Product entity = repository.getById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Product entity, Product obj) {
		entity.setName(obj.getName());
		entity.setPrice(obj.getPrice());
		entity.setDescription(obj.getDescription());

		if (obj.getCategory() != null) {
			entity.setCategory(obj.getCategory());
		} else {
			entity.getCategory().setName(obj.getCategory().getName());
		}
	}

}
