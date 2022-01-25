package com.example.demo.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	public List<Product> findAll() {
		return repository.findAll();
	}

	public Product findById(Long id) throws ProductNotFoundException {
		Product obj = repository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
		return obj;
	}

	public List<Product> findProductByCategory(String category) throws ProductNotFoundException {
		if (repository.findProductsByCategory(category).isEmpty() == true) {
			throw new ProductNotFoundException(category + " not found.");
		}
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
	public Product update(Long id, Product obj) throws ProductNotFoundException {
		if(repository.existsById(id) == false) {
			throw new ProductNotFoundException("Product with id "+id+" not found.");
		}
		Product entity = repository.getById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Product entity, Product obj) {
		entity.setName(obj.getName());
		entity.setUrlImage(obj.getUrlImage());
		entity.setPrice(obj.getPrice());
		entity.setDescription(obj.getDescription());

		if (obj.getCategory() != null) {
			entity.setCategory(obj.getCategory());
		} else {
			entity.getCategory().setName(obj.getCategory().getName());
		}
	}

}
