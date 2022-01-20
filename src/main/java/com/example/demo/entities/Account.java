package com.example.demo.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(unique = true)
	private String email;
	@Basic(optional = false)
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
