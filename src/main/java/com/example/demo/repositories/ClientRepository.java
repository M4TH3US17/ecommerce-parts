package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	@Query("SELECT c from Client c WHERE c.email = :email")
	Optional<Client> findByEmail(@Param("email") String email);
}
