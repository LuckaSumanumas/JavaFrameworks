package com.deepmedia.javaframeworks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.deepmedia.javaframeworks.entities.UserAuth;
import com.deepmedia.javaframeworks.gateways.JavaFrameworksGateway;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


//@RunWith(PowerMockRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
//@PrepareForTest(JavaFrameworksGateway.class)
public class JavaFrameworksControllerTest {

	@LocalServerPort
    int randomServerPort;
     
//	@MockBean
//    private JavaFrameworksGateway gateway;
	
	@Value( "${github.api.search.frameworks}" )
	private String searchStr;
	
	@Before
	public void setUp() {
		//MockitoAnnotations.initMocks(this);
	}
	
    @Test
    public void testRetrieveFrameworksSuccess() throws Exception 
    {
//    	JavaFrameworksGateway gateway = mock(JavaFrameworksGateway.class);
//    	UserAuth auth = new UserAuth(null, null);
//    	PowerMockito.whenNew(JavaFrameworksGateway.class).withArguments(Mockito.anyString(), Mockito.any()).thenReturn(gateway);
//    	
//    	List<GithubResponseItem> repoItems = new ArrayList<>();
//    	Mockito.when(gateway.retrieveSearchedRepoItems(searchStr)).thenReturn(repoItems);
//    	List<GithubResponseItem> starredRepos = new ArrayList<>();
//    	Mockito.when(gateway.retrieveUserStarredRepos()).thenReturn(starredRepos);
//    	Mockito.when(gateway.retrieveNumberOfContributors(ArgumentMatchers.anyString())).thenReturn(0);
//    	
//    	
//    	RestTemplate restTemplate = new RestTemplate();
//    	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//    	Type listType = new TypeToken<ArrayList<JavaFramework>>(){}.getType();
//		
//    	
//    	final String baseUrl = "http://localhost:" + randomServerPort + "/api/java-frameworks";
//        URI uri = new URI(baseUrl);
//        ResponseEntity<String> repsonse = restTemplate.getForEntity(uri, String.class);
//        
//        Assert.assertEquals(200, repsonse.getStatusCodeValue());
//        
//        List<JavaFramework> itemList = gson.fromJson(repsonse.getBody(), listType);
//        Assert.assertEquals(true, itemList.size()==10);
    }  
}
