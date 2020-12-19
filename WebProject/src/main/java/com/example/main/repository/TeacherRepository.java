package com.example.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
