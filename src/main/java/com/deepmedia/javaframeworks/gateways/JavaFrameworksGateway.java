package com.deepmedia.javaframeworks.gateways;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
public class JavaFrameworksGateway {
	
	private RestTemplate restTemplate;
	private Gson gson;
	private String baseUrl;
	private UserAuth auth;
	
	public JavaFrameworksGateway(String baseUrl, UserAuth auth) {
		restTemplate = new RestTemplate();
		gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		this.baseUrl = baseUrl;
		this.auth = auth;
	}

	public List<GithubResponseItem> retrieveSearchedRepoItems(String searchStr) {
		String url = baseUrl + searchStr;
		
		HttpEntity<String> entity = new HttpEntity<String>(collectHeader());
		String responseStr = restTemplate.exchange(
				url, HttpMethod.GET, entity, String.class).getBody();
		
		JsonElement jsonElement = JsonParser.parseString(responseStr).getAsJsonObject().get("items");
		List<GithubResponseItem> itemList = retrieveListOfItems(jsonElement);
		
		return itemList;
	}
	
	public Integer retrieveNumberOfContributors(String contrUrl) {
		String url = contrUrl + "?per_page=1&anon=true";
		
		HttpEntity<String> entity = new HttpEntity<String>(collectHeader());
		ResponseEntity<String> response = restTemplate.exchange(
				url, HttpMethod.GET, entity, String.class);
		
		List<String> links = response.getHeaders().get("link");
		String numberOfContrStr = links.get(0).split(",")[1].split("page")[2].split("=")[1].split(">")[0];
		Integer numberOfContr = Integer.valueOf(numberOfContrStr);
		
		return numberOfContr;

	}

	public List<GithubResponseItem> retrieveUserStarredRepos() {
		List<GithubResponseItem> itemList = new ArrayList<>();
		
		if(auth.getUsername() != null) {
			String url = baseUrl + "/users/" + auth.getUsername() + "/starred";
			
			HttpEntity<String> entity = new HttpEntity<String>(collectHeader());
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

	public Boolean changeRepoStar(String owner, String repo, Boolean star) {
		Boolean isSuccessStarring = false;
		Boolean isStarred = invokeRepoStar(owner, repo, HttpMethod.GET);
	
		if(isStarred && !star) {
			//if repo starred and requrest for unstar then unstar
			isSuccessStarring = invokeRepoStar(owner, repo, HttpMethod.DELETE);
		} else if(!isStarred && star) {
			//if repo is not starred and requrest for star then star
			isSuccessStarring = invokeRepoStar(owner, repo, HttpMethod.PUT);
		}
		
		return isSuccessStarring;
	}
	
	private Boolean invokeRepoStar(String owner, String repo, HttpMethod method) {
		String url = baseUrl + "/user/starred/" + owner + "/" + repo;
		
		HttpEntity<String> entity = new HttpEntity<String>(collectHeader());
		
		try {
			ResponseEntity<String> response = 
				restTemplate.exchange(url, method, entity, String.class);
			return response.getStatusCode().is2xxSuccessful();
		} catch(HttpClientErrorException e) {
			return false;
		}
		
	}
	
	private HttpHeaders collectHeader() {
		HttpHeaders headers = new HttpHeaders();
		
		if(auth.getUsername() != null && auth.getPassword() != null) {
			String authStr = auth.getUsername() + ":" + auth.getPassword();
			String encoded = Base64.getEncoder().encodeToString(authStr.getBytes());
	        headers.set("Authorization", "Basic " + encoded);
		}
		
        return headers;
	}

	
}
