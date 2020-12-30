package com.example.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.AppConfig;
import com.example.main.model.Cours;
import com.example.main.repository.CoursRepository;

@RestController
@RequestMapping("/api/v1/cours/")
@CrossOrigin(origins = "http://localhost:4200")
public class CoursController extends AppConfig {

	@Autowired
	private CoursRepository coursRepository;

	// get All Cours
	@GetMapping("/getAllCours")
	public List<Cours> getAllCours() {
		return coursRepository.findAll();
	}

	// save Course
	@PostMapping("/saveCours")
	public ResponseEntity<Cours> saveCours(@PathVariable("coursData") Cours cours) {
		try {
			Optional<Cours> coursData = coursRepository.findById(cours.getId());
			if (coursData.isPresent()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				coursRepository.save(cours);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// get Cours by Id
	@GetMapping("/getCoursById/{coursId}")
	public ResponseEntity<Cours> getCoursById(@PathVariable("coursId") Long id) {
		try {
			Optional<Cours> coursData = coursRepository.findById(id);
			if (coursData.isPresent()) {
				return new ResponseEntity<>(coursData.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// delete Cours by Id
	@GetMapping("/deleteCoursById/{courseId}")
	public ResponseEntity<Cours> deleteCoursById(@PathVariable("courseId") Long id) {
		try {
			Optional<Cours> coursData = coursRepository.findById(id);
			if (coursData.isPresent()) {
				coursRepository.deleteById(id);
				return new ResponseEntity<>(coursData.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// update cours
	@PutMapping("/updateCours/{id}")
	public ResponseEntity<Cours> updateCours(@PathVariable("id") Long id, @RequestBody Cours cours) {
		Optional<Cours> coursData = coursRepository.findById(id);
		if (coursData.isPresent()) {
			Cours _cours = coursData.get();
			_cours.setSubject(cours.getSubject());
			_cours.setTeacher(cours.getTeacher());
			return new ResponseEntity<>(coursRepository.save(_cours), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
