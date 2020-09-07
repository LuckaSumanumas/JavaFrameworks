package com.deepmedia.javaframeworks;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deepmedia.javaframeworks.entities.JavaFramework;
import com.deepmedia.javaframeworks.entities.KeyMetrics;
import com.deepmedia.javaframeworks.entities.UserAuth;
import com.deepmedia.javaframeworks.gateways.JavaFrameworksGateway;
import com.deepmedia.javaframeworks.services.JavaFrameworksService;

/**
 * JavaFrameworksService unit test
 * 
 * @author Mindaugas Lucka
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaFrameworksServiceTest {
	
	private UserAuth auth;
	
	@InjectMocks
	private JavaFrameworksService service;
	
	@Mock
	JavaFrameworksGateway gateway;
	
	@Before
	public void setUp() {
		auth = new UserAuth(null, null);
		TestHelper.mockGateway(gateway, auth);
	}
	
	@Test
    public void testSortFrameworksByStarCount() {
		
		List<JavaFramework> frameworks = service.retrieveJavaFrameworks(
				KeyMetrics.STAR_COUNT.getName(), auth);
		
		assertTrue(frameworks.size()==3);
		assertTrue(frameworks.get(0).getStarCount()==10);
		assertTrue(frameworks.get(2).getStarCount()==20);
	}
	
	@Test
    public void testSortFrameworksByNumOfContr() {
		
		List<JavaFramework> frameworks = service.retrieveJavaFrameworks(
				KeyMetrics.NUM_OF_CONTR.getName(), auth);
		
		assertTrue(frameworks.size()==3);
		assertTrue(frameworks.get(0).getNumOfContr()==3);
		assertTrue(frameworks.get(2).getNumOfContr()==7);
	}
	
}
