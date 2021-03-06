package com.example.demo.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Product;
import com.example.demo.services.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/products")
public class ProductResource {

	@Autowired
	private ProductService service;
	
	@ApiOperation("Retorna uma lista paginada de produtos")
	@GetMapping(value = "/pagination", produces = "application/json")
	public ResponseEntity<Page<Product>> findProductsByPage(@PageableDefault(size = 10)Pageable peageble){
		return ResponseEntity.ok().body(service.findByPage(peageble));
	}

	@ApiOperation("Pesquisa um produto por id")
	@ApiResponses({@ApiResponse(code = 404, message = "Produto com ID informado não existe")})
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Product> findById(@PathVariable Long id) throws Exception {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@ApiOperation("Pesquisa um produto a partir do nome de uma categoria")
	@ApiResponses({@ApiResponse(code = 404, message = "Nenhum produto pertecente a categoria \"nomeCategoria\" foi encontrado")})
	@GetMapping(value = "/category/{category}")
	public ResponseEntity<List<Product>> findProductByCategory(@PathVariable("category") String category)
			throws Exception {
		return ResponseEntity.ok().body(service.findProductByCategory(category));
	}

	@ApiOperation("Salva um produto")
	@ApiResponses({@ApiResponse(code = 403, message = "Erro Forbidden")})
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Product> save(@Valid @RequestBody Product obj) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(obj));
	}

	@ApiOperation("Delete um produto por id")
	@ApiResponses({@ApiResponse(code = 403, message = "Erro Forbidden")})
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ApiOperation("Atualiza um produto")
	@ApiResponses({@ApiResponse(code = 404, message = "Produto com o id informado não existe"), @ApiResponse(code = 403, message = "Erro Forbidden")})
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product obj) throws Exception {
		return ResponseEntity.ok().body(service.update(id, obj));
	}
}
