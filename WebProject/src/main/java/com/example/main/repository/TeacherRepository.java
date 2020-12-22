package com.example.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	public Optional<Teacher> getTeacherByEmailIdAndPassword(String emailId, String password);

	public Optional<Teacher> getTeacherByEmailId(String emailId);

}
