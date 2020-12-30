package com.example.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.AppConfig;
import com.example.main.model.Student;
import com.example.main.repository.StudentRepository;

@RestController
@RequestMapping("/api/v1/student/")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController extends AppConfig {

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private JavaMailSender emailSender;

	// get All Student
	@GetMapping("/getStudents")
	public List<Student> getAllStudent() {
		return studentRepository.findAll();
	}

	@GetMapping("/getStudentByEmailAndPassword/{emailId}/{password}")
	public ResponseEntity<Student> getStudentByEmailAndPassword(@PathVariable("emailId") String emailId,
			@PathVariable("password") String password) {
		Optional<Student> studentData = studentRepository.getStudentByEmailIdAndPassword(emailId, password);
		if (studentData.isPresent()) {
			return new ResponseEntity<>(studentData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// save student
	@PostMapping("/saveStudent")
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		try {
			try {
				// check if there is a user by this email in the database
				Optional<Student> studentData = studentRepository.getStudentByEmailId(student.getEmailId());
				if (studentData.isPresent()) {
					return new ResponseEntity<>(student, HttpStatus.NO_CONTENT);
				} else {
					studentRepository.save(student);
					SimpleMailMessage message = new SimpleMailMessage();
					message.setFrom("mensikhalil@gmail.com");
					message.setTo(student.getEmailId());
					message.setSubject("Password");
					message.setText(student.getPassword());
					emailSender.send(message);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseEntity<>(student, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(student, HttpStatus.NO_CONTENT);
		}
	}

	// get student by Id
	@GetMapping("/getStudent/{studentId}")
	public ResponseEntity<Student> getStudentById(@PathVariable("studentId") Long id) {
		Optional<Student> studentData = studentRepository.findById(id);
		if (studentData.isPresent()) {
			return new ResponseEntity<>(studentData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// delete Student by Id
	@GetMapping("/deleteStudent/{studentId}")
	public ResponseEntity<HttpStatus> deleteStudentById(@PathVariable("studentId") Long id) {
		try {
			studentRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// update student
	@PutMapping("/updateStudent/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @RequestBody Student student) {
		Optional<Student> studentData = studentRepository.findById(id);
		if (studentData.isPresent()) {
			Student _student = studentData.get();
			_student.setFirstName(student.getFirstName());
			_student.setLastName(student.getLastName());
			_student.setEmailId(student.getEmailId());
			_student.setPassword(student.getPassword());
			return new ResponseEntity<>(studentRepository.save(_student), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
