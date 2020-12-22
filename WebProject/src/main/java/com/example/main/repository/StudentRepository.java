package com.example.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	public Optional<Student> getStudentByEmailIdAndPassword(String emailId, String password);

	public Optional<Student> getStudentByEmailId(String emailId);
}
