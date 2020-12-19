package com.example.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.model.Teacher;
import com.example.main.repository.TeacherRepository;

@RestController
@RequestMapping("/api/v1/")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepository;

	@GetMapping("/teachers")
	public List<Teacher> getAllTeacher() {
		return teacherRepository.findAll();
	}
}
