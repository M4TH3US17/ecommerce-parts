package com.example.demo.resources;

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

import com.example.demo.entities.Category;
import com.example.demo.services.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
@RestController
@CrossOrigin("*")
@RequestMapping("/api/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;
	
	@ApiOperation("Retorna uma lista paginada de categorias")
	@ApiResponses({@ApiResponse(code = 403, message = "Erro Forbidden")})
	@GetMapping(value = "/pagination", produces = "application/json")
	public ResponseEntity<Page<Category>> findCategoriesByPage(@PageableDefault(size = 10)Pageable pageable){
		return ResponseEntity.ok().body(service.findByPage(pageable));
	}

	@ApiOperation("Pesquisa uma categoria por id")
	@ApiResponses({@ApiResponse(code = 403, message = "Erro Forbidden"), @ApiResponse(code = 404, message = "Categoria com o ID informado não existe")})
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Category> findById(@PathVariable Long id) throws Exception {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@ApiOperation("Salva uma categoria")
	@ApiResponses({@ApiResponse(code = 201, message = "Categoria salva com sucesso"), @ApiResponse(code = 403, message = "Erro Forbidden")})
	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Category> save(@Valid @RequestBody Category obj) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(obj));
	}

	@ApiOperation("Deleta uma categoria por id")
	@ApiResponses({@ApiResponse(code = 404, message = "Categoria com o ID informado não existe"), @ApiResponse(code = 403, message = "Erro Forbidden")})
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) throws Exception {
		service.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ApiOperation("Atualiza uma categoria")
	@ApiResponses({@ApiResponse(code = 404, message = "Categoria com ID informado não existe"), @ApiResponse(code = 403, message = "Erro Forbidden")})
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Category> update(@PathVariable Long id, @Valid @RequestBody Category obj) throws Exception {
		return ResponseEntity.ok().body(service.update(id, obj));
	}
}
