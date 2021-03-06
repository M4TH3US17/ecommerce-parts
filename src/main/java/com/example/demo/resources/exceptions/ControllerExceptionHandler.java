package com.example.demo.resources.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.resources.exceptions.verification.PasswordInvalidException;
import com.example.demo.services.exceptions.notfound.CategoryNotFoundException;
import com.example.demo.services.exceptions.notfound.ClientNotFoundException;
import com.example.demo.services.exceptions.notfound.ProductNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	@ExceptionHandler({ ProductNotFoundException.class, ClientNotFoundException.class, CategoryNotFoundException.class,
			NoSuchElementException.class })
	protected ResponseEntity<ErrorDetails> entityNotFoundException(Exception ex) {
		ErrorDetails erro = new ErrorDetails(LocalDateTime.now().format(formatter), HttpStatus.NOT_FOUND.value(),
				ex.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}

	@ExceptionHandler({ ConstraintViolationException.class, DataIntegrityViolationException.class,
			EmptyResultDataAccessException.class })
	protected ResponseEntity<ErrorDetails> dataViolationException(Exception ex) {
		ErrorDetails erro = new ErrorDetails(LocalDateTime.now().format(formatter), HttpStatus.CONFLICT.value(),
				"Database violation: this operation may generate conflicts in the database.");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	protected ResponseEntity<Map<String, ErrorDetails>> validationException(MethodArgumentNotValidException ex) {
		Map<String, ErrorDetails> map = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldError = ((FieldError) error).getField();
			ErrorDetails err = new ErrorDetails();
			err.setError(error.getDefaultMessage());
			err.setTimestamp(LocalDateTime.now().format(formatter));
			err.setStatus(HttpStatus.BAD_REQUEST.value());
			map.put(fieldError, err);
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
	}

	@ExceptionHandler({ PasswordInvalidException.class, ResponseStatusException.class })
	public ResponseEntity<ErrorDetails> credentialsInvalid(Exception e) {
		String err = e.getMessage();
		ErrorDetails error = new ErrorDetails();
		error.setTimestamp(LocalDateTime.now().format(formatter));
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		if (e.getMessage().equals("400 BAD_REQUEST \"check that the credentials (email and password) are correct.\"")) {
			err = "invalid email.";
		}
		error.setError(err);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorDetails> urlInvalidException(Exception e) {
		ErrorDetails error = new ErrorDetails();
		error.setTimestamp(LocalDateTime.now().format(formatter));
		error.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
		error.setError("non-existing URL");
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
	}
}
