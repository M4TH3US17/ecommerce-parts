package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	//@Query("SELECT c from Client c JOIN c.role r WHERE r.name = :name")
	Role findByName(String name);
}
