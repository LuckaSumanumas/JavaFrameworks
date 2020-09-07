package com.deepmedia.javaframeworks.gateways;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.deepmedia.javaframeworks.entities.GithubResponseItem;
import com.deepmedia.javaframeworks.entities.UserAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author Mindaugas Lucka
 *
 */
@Component
public class JavaFrameworksGateway {
	
	@Value( "${github.api.baseurl}" )
	private String baseUrl;
	
	@Value( "${github.api.search.frameworks}" )
	private String searchStr;
	
	@Autowired
	private RestTemplate restTemplate;
	private Gson gson;
	
	public JavaFrameworksGateway() {
		gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}

	public List<GithubResponseItem> retrieveSearchedRepoItems(UserAuth auth) {
		String url = baseUrl + searchStr;
		
		HttpEntity<String> entity = new HttpEntity<String>(collectHeader(auth));
		String responseStr = restTemplate.exchange(
				url, HttpMethod.GET, entity, String.class).getBody();
		
		JsonElement jsonElement = JsonParser.parseString(responseStr).getAsJsonObject().get("items");
		List<GithubResponseItem> itemList = retrieveListOfItems(jsonElement);
		
		return itemList;
	}
	
	public Integer retrieveNumberOfContributors(String contrUrl, UserAuth auth) {
		String url = contrUrl + "?per_page=1&anon=true";
		
		HttpEntity<String> entity = new HttpEntity<String>(collectHeader(auth));
		ResponseEntity<String> response = restTemplate.exchange(
				url, HttpMethod.GET, entity, String.class);
		
		List<String> links = response.getHeaders().get("link");
		String numberOfContrStr = links.get(0).split(",")[1].split("page")[2].split("=")[1].split(">")[0];
		Integer numberOfContr = Integer.valueOf(numberOfContrStr);
		
		return numberOfContr;

	}

	public List<GithubResponseItem> retrieveUserStarredRepos(UserAuth auth) {
		List<GithubResponseItem> itemList = new ArrayList<>();
		
		if(auth.getUsername() != null) {
			String url = baseUrl + "/users/" + auth.getUsername() + "/starred";
			
			HttpEntity<String> entity = new HttpEntity<String>(collectHeader(auth));
			String responseStr = restTemplate.exchange(
					url, HttpMethod.GET, entity, String.class).getBody();
			Type listType = new TypeToken<ArrayList<GithubResponseItem>>(){}.getType();
			itemList = gson.fromJson(responseStr, listType); 
		} 
		
		return itemList;
		
	}
	
	
	private List<GithubResponseItem> retrieveListOfItems(JsonElement jsonElement) {
		
		List<GithubResponseItem> itemList = new ArrayList<>();
		
		if (jsonElement.isJsonObject()) {
			// The returned list has only 1 element
			GithubResponseItem item = gson.fromJson(jsonElement, GithubResponseItem.class);
			itemList.add(item);
		} else if (jsonElement.isJsonArray()) {
			// The returned list has more than 1 element
			Type listType = new TypeToken<List<GithubResponseItem>>() {
			}.getType();
			itemList = gson.fromJson(jsonElement, listType);
		}

		return itemList;
	}

	public Boolean changeRepoStar(
			String owner, String repo, Boolean star, UserAuth auth) {
		Boolean isSuccessStarring = false;
		Boolean isStarred = invokeRepoStar(owner, repo, HttpMethod.GET, auth);
	
		if(isStarred && !star) {
			//if repo starred and request for unstar then unstar
			isSuccessStarring = invokeRepoStar(owner, repo, HttpMethod.DELETE, auth);
		} else if(!isStarred && star) {
			//if repo is not starred and requrest for star then star
			isSuccessStarring = invokeRepoStar(owner, repo, HttpMethod.PUT, auth);
		}
		
		return isSuccessStarring;
	}
	
	private Boolean invokeRepoStar(
			String owner, String repo, HttpMethod method, UserAuth auth) {
		String url = baseUrl + "/user/starred/" + owner + "/" + repo;
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<String> entity = new HttpEntity<String>(collectHeader(auth));
		
		try {
			ResponseEntity<String> response = 
				restTemplate.exchange(url, method, entity, String.class);
			return response.getStatusCode().is2xxSuccessful();
		} catch(HttpClientErrorException e) {
			return false;
		}
		
	}
	
	private HttpHeaders collectHeader(UserAuth auth) {
		HttpHeaders headers = new HttpHeaders();
		
		if(auth.getUsername() != null && auth.getPassword() != null) {
			String authStr = auth.getUsername() + ":" + auth.getPassword();
			String encoded = Base64.getEncoder().encodeToString(authStr.getBytes());
	        headers.set("Authorization", "Basic " + encoded);
		}
		
        return headers;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.build();
	}
}
