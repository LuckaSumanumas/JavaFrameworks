package com.deepmedia.javaframeworks.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deepmedia.javaframeworks.entities.JavaFramework;
import com.deepmedia.javaframeworks.entities.Starring;
import com.deepmedia.javaframeworks.entities.UserAuth;
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
	
	private Logger logger = LoggerFactory.getLogger(JavaFrameworksController.class);
	
	@GetMapping("/java-frameworks")
	public ResponseEntity<?> retrieveFrameworks(
			@RequestHeader Map<String, String> headers,
			@RequestParam(required = false) String metric) {
		
		UserAuth auth = new UserAuth(headers.get("username"), headers.get("password"));
		
		try {
			List<JavaFramework> response = service.retrieveJavaFrameworks(metric, auth);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			logger.error("Failed to retrieve java frameworks details: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PatchMapping("/java-frameworks/{name}")
	public ResponseEntity<?> updateRepoStarring(
			@RequestHeader Map<String, String> headers,
			@PathVariable String name,
			@RequestBody Starring starring) {
		
		UserAuth auth = new UserAuth(headers.get("username"), headers.get("password"));
		
		try {
			String response = service.starJavaFrameworkRepo(name, starring, auth);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			logger.error("Failed to change starring for java framework " + name + ": " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
	
}
