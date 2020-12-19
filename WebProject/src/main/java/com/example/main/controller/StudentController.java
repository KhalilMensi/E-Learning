package com.example.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.model.Student;
import com.example.main.repository.StudentRepository;

@RestController
@RequestMapping("/api/v1/")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	// get All Student
	@GetMapping("/students")
	public List<Student> getAllStudent() {
		return studentRepository.findAll();
	}
}
