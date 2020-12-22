package com.example.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.model.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
	public Optional<Administrator> getAdministratorByEmailIdAndPassword(String emailId, String password);

	public Optional<Administrator> getAdministratorByEmailId(String emailId);
}
