package com.example.demo.resources.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.services.exceptions.notfound.CategoryNotFoundException;
import com.example.demo.services.exceptions.notfound.ClientNotFoundException;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@ExceptionHandler({ProductNotFoundException.class, ClientNotFoundException.class, CategoryNotFoundException.class})
	protected ResponseEntity<ErrorDetails> entityNotFoundException(Exception ex) {
		ErrorDetails erro = new ErrorDetails( LocalDateTime.now().format(formatter), HttpStatus.NOT_FOUND.value(),
				ex.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class, EmptyResultDataAccessException.class})
	protected ResponseEntity<ErrorDetails> dataViolationException(Exception ex) {
		ErrorDetails erro = new ErrorDetails( LocalDateTime.now().format(formatter), HttpStatus.CONFLICT.value(),
				"Database violation: this operation may generate conflicts in the database.");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}
	
 /*@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<List<ErrorDetails>> validationException (MethodArgumentNotValidException e){
		List<ErrorDetails> error = new ArrayList<>();
		e.getBindingResult().getFieldErrors().forEach(x -> {
			ErrorDetails err = new ErrorDetails(LocalDateTime.now().format(formatter),
					HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage());
			error.add(err);
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}*/
}
