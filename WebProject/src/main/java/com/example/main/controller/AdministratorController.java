package com.example.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.model.Administrator;
import com.example.main.repository.AdministratorRepository;

@RestController
@RequestMapping("/api/v1/")
public class AdministratorController {
	@Autowired
	private AdministratorRepository administratorRepository;

	@GetMapping("/administrators")
	public List<Administrator> getAllAdministrator() {
		return administratorRepository.findAll();
	}
}
