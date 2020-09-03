package com.deepmedia.javaframeworks;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import com.deepmedia.javaframeworks.gateways.JavaFrameworksGateway;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestClientTest(JavaFrameworksGateway.class)
public class JavaFrameworksGatewayTest {

	@Autowired
	private JavaFrameworksGateway gateway;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	@Test
	public void testRetrieveSearchedRepoItems() {
		
	}
}
