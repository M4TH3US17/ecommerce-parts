package com.example.demo.resources.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.services.exceptions.notfound.CategoryNotFoundException;
import com.example.demo.services.exceptions.notfound.ClientNotFoundException;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@ExceptionHandler({ProductNotFoundException.class, ClientNotFoundException.class, CategoryNotFoundException.class})
	protected ResponseEntity<ErrorDetails> entityNotFoundException(Exception ex) {
		ErrorDetails erro = new ErrorDetails(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND.value(),
				 LocalDateTime.now().format(formatter));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
}
