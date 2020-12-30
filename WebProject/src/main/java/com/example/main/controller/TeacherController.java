package com.example.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.AppConfig;
import com.example.main.model.Teacher;
import com.example.main.repository.TeacherRepository;

@RestController
@RequestMapping("/api/v1/teacher/")
public class TeacherController extends AppConfig {

	@Autowired
	private TeacherRepository teacherRepository;

	private JavaMailSender emailSender;

	@GetMapping("/getTeachers")
	public List<Teacher> getAllTeacher() {
		return teacherRepository.findAll();
	}

	// save teacher
	@PostMapping("/saveTeacher")
	public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
		try {
			Optional<Teacher> teacherData = teacherRepository.getTeacherByEmailId(teacher.getEmailId());
			if (teacherData.isPresent()) {
				return new ResponseEntity<>(teacherData.get(), HttpStatus.FORBIDDEN);
			} else {
				teacherRepository.save(teacher);
				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom("mensikhalil@gmail.com");
				message.setTo(teacher.getEmailId());
				message.setSubject("Password");
				message.setText(teacher.getPassword());
				emailSender.send(message);
				return new ResponseEntity<>(teacher, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(teacher, HttpStatus.NO_CONTENT);
		}
	}

	// get teacher by Id
	@GetMapping("/getTeacher/{teacherId}")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("teacherId") Long id) {
		Optional<Teacher> teacherData = teacherRepository.findById(id);
		if (teacherData.isPresent()) {
			return new ResponseEntity<>(teacherData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getTeacherByEmailAndPassword/{emailId}/{password}")
	public ResponseEntity<Teacher> getTeacherByEmailAndPassword(@PathVariable("emailId") String emailId,
			@PathVariable("password") String password) {
		Optional<Teacher> teacherData = teacherRepository.getTeacherByEmailIdAndPassword(emailId, password);
		if (teacherData.isPresent()) {
			return new ResponseEntity<>(teacherData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// delete Teacher by Id
	@GetMapping("/deleteTeacher/{teacherId}")
	public ResponseEntity<HttpStatus> deleteTeacherById(@PathVariable("teacherId") Long id) {
		try {
			teacherRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// update teacher
	@PutMapping("/updateTeacher/{id}")
	public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") Long id, @RequestBody Teacher teacher) {
		Optional<Teacher> teacherData = teacherRepository.findById(id);
		if (teacherData.isPresent()) {
			Teacher _teacher = teacherData.get();
			_teacher.setFirstName(teacher.getFirstName());
			_teacher.setLastName(teacher.getLastName());
			_teacher.setEmailId(teacher.getEmailId());
			_teacher.setPassword(teacher.getPassword());
			return new ResponseEntity<>(teacherRepository.save(_teacher), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
