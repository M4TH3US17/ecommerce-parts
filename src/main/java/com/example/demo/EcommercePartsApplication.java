package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class EcommercePartsApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(EcommercePartsApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("**")
				.allowedMethods("POST", "PUT", "GET", "DELETE")
				.allowedOrigins("*");
	}
}
