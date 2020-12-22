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
import com.example.main.model.Administrator;
import com.example.main.repository.AdministratorRepository;

@RestController
@RequestMapping("/api/v1/")
public class AdministratorController extends AppConfig {
	@Autowired
	private AdministratorRepository administratorRepository;
	@Autowired
	private JavaMailSender emailSender;

	@GetMapping("/getAdministrators")
	public List<Administrator> getAllAdministrator() {
		return administratorRepository.findAll();
	}

	// save Administrator
	@PostMapping("/saveAdministrator")
	public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator administrator) {
		try {
			// TODO verification de l'existance d'un autre utilisateur avec les méme donnée
			Optional<Administrator> administratorData = administratorRepository
					.getAdministratorByEmailId(administrator.getEmailId());
			if (administratorData.isPresent()) {
				return new ResponseEntity<>(administrator, HttpStatus.BAD_REQUEST);
			} else {
				administratorRepository.save(administrator);
				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom("mensikhalil@gmail.com");
				message.setTo(administrator.getEmailId());
				message.setSubject("Password");
				message.setText(administrator.getPassword());
				emailSender.send(message);
				return new ResponseEntity<>(administrator, HttpStatus.CREATED);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(administrator, HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/getAdministratorByEmailIdAndPassword/{emailId}/{password}")
	public ResponseEntity<Administrator> getAdministratorByEmailIdAndPassword(@PathVariable("emailId") String emailId,
			@PathVariable("password") String password) {
		Optional<Administrator> administratorData = administratorRepository
				.getAdministratorByEmailIdAndPassword(emailId, password);
		if (administratorData.isPresent()) {
			return new ResponseEntity<>(administratorData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// get administrator by Id
	@GetMapping("/getAdministrator/{AdministratorId}")
	public ResponseEntity<Administrator> getAdministratorById(@PathVariable("AdministratorId") Long id) {
		Optional<Administrator> administratorData = administratorRepository.findById(id);
		if (administratorData.isPresent()) {
			return new ResponseEntity<>(administratorData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// delete Administrator by Id
	@GetMapping("/deleteAdministrator/{administratorId}")
	public ResponseEntity<HttpStatus> deleteAdministratorById(@PathVariable("administratorId") Long id) {
		try {
			administratorRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// update Administrator
	@PutMapping("/updateAdministrator/{id}")
	public ResponseEntity<Administrator> updateAdministrator(@PathVariable("id") Long id,
			@RequestBody Administrator administrator) {
		Optional<Administrator> administratorData = administratorRepository.findById(id);
		if (administratorData.isPresent()) {
			Administrator _administrator = administratorData.get();
			_administrator.setFirstName(administrator.getFirstName());
			_administrator.setLastName(administrator.getLastName());
			_administrator.setEmailId(administrator.getEmailId());
			_administrator.setPassword(administrator.getPassword());
			return new ResponseEntity<>(administratorRepository.save(_administrator), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
