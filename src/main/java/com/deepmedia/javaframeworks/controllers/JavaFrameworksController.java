package com.deepmedia.javaframeworks.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deepmedia.javaframeworks.entities.GithubResponseItem;
import com.deepmedia.javaframeworks.entities.JavaFramework;
import com.deepmedia.javaframeworks.services.JavaFrameworksService;

@RestController
@RequestMapping("/api")
public class JavaFrameworksController {

	@Autowired
    private JavaFrameworksService service;
	
//	@GetMapping("/contracts")
//	public ResponseEntity<Response<?>> retrieveContracts() {
//		try {
//			List<Contract> contracts = contractService.retrieveContracts();
//			return new ResponseBuilder<Contract>("success", "contracts", contracts).build();
//		} catch (Exception e) {
//			return new ResponseBuilder<Contract>("error", "contracts", HttpStatus.NOT_FOUND, e.getMessage()).build();
//		}
//	}
	
	
	@GetMapping("/frameworks")
	public ResponseEntity<?> retrieveFrameworks(
			@RequestParam(required = false) String metric) {
		try {
			List<JavaFramework> response = service.retrieveListOfJavaFrameworks(metric);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			System.out.println("failure: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
			
	}
	
	
}
