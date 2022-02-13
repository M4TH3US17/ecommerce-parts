package com.example.demo.configurations.jwt.dto;

import java.io.Serializable;

public class TokenDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String token;
	
	public TokenDTO() {
	}

	public TokenDTO(String name, String token) {
		this.name = name;
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
