package com.deepmedia.javaframeworks.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deepmedia.javaframeworks.entities.JavaFramework;
import com.deepmedia.javaframeworks.entities.Starring;
import com.deepmedia.javaframeworks.services.JavaFrameworksService;

/**
 * 
 * @author Mindaugas Lucka
 *
 */
@RestController
@RequestMapping("/api")
public class JavaFrameworksController {

	@Autowired
    private JavaFrameworksService service;
	
	@GetMapping(value = "/java-frameworks")
	public ResponseEntity<?> retrieveFrameworks(
			@RequestParam(required = false) String metric) {
		try {
			List<JavaFramework> response = service.retrieveJavaFrameworks(metric);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			System.out.println("failure: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/java-frameworks/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateRepoStarring(
			@PathVariable String name,
			@RequestBody Starring starring) {
		try {
			String response = service.starJavaFrameworkRepo(name, starring);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
	
}
