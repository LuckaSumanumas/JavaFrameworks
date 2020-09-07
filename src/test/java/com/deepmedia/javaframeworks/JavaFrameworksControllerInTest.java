package com.deepmedia.javaframeworks;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.deepmedia.javaframeworks.entities.JavaFramework;
import com.deepmedia.javaframeworks.entities.Starring;
import com.deepmedia.javaframeworks.entities.UserAuth;
import com.deepmedia.javaframeworks.gateways.JavaFrameworksGateway;

/**
 * JavaFrameworksController integration test
 * 
 * @author Mindaugas Lucka
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class JavaFrameworksControllerInTest {
	
	private RestTemplate restTemplate;
	
	private UserAuth auth;
	
	@MockBean
	private JavaFrameworksGateway gateway;
	
	@LocalServerPort
    private int randomPort;
	
	@Before
	public void setUp() {
		restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		auth = new UserAuth(null, null);
		TestHelper.mockGateway(gateway, auth);
	}
	
    @Test
    public void testRetrieveFrameworksSuccess() throws Exception 
    {	
    	final String baseUrl = "http://localhost:" + randomPort + "/api/java-frameworks";
        URI uri = new URI(baseUrl);
        ResponseEntity<JavaFramework[]> repsonse = 
        		restTemplate.getForEntity(uri, JavaFramework[].class);
        JavaFramework[] frameworks = repsonse.getBody();
        
        Assert.assertEquals(200, repsonse.getStatusCodeValue());
        Assert.assertTrue(frameworks.length==3);
    }  
    
	@Test
	public void testStarJavaFrameworkRepoSuccess() throws Exception  {
		final String url = "http://localhost:" + randomPort + "/api/java-frameworks/{name}";
		
		Starring starring = new Starring();
		
		starring.setOwner("owner");
		starring.setStar(true);
		
		HttpEntity<Starring> entity = new HttpEntity<Starring>(starring);
		
		Map<String, String> urlParams = new HashMap<>();
		urlParams.put("name", "framework-1");
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
		
		URI uri = uriBuilder.buildAndExpand(urlParams).toUri();
        ResponseEntity<String> repsonse = 
        		restTemplate.exchange(
        				uri, HttpMethod.PATCH, entity, String.class);

        Assert.assertEquals(200, repsonse.getStatusCodeValue());
        Assert.assertTrue(repsonse.getBody().equals("Repository successfuly starred"));
	}
    
}
