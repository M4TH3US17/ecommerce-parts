package com.example.demo.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Email(message = "{account.email.not.valid}")
	@Column(unique = true, nullable = false)
	private String email;
	@NotBlank(message = "{account.password.not.blank}")
	@Column(nullable = false)
	private String password;
	
	public Account() {
	}

	public Account(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
