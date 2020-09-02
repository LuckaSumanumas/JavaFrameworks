package com.deepmedia.javaframeworks.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deepmedia.javaframeworks.entities.GithubResponseItem;
import com.deepmedia.javaframeworks.entities.JavaFramework;
import com.deepmedia.javaframeworks.entities.KeyMetrics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author Mindaugas Lucka
 * 
 * Service for retrieving java frameworks from GitHub
 *
 */
@Service
public class JavaFrameworksService {

	final private String javaFrameworks = "https://api.github.com/search/repositories?q=framework in:name,description+language:java&per_page=10&sort=stars&order=desc";
	private RestTemplate restTemplate = new RestTemplate();
	
	public List<JavaFramework> retrieveListOfJavaFrameworks(String metric) {

		String responseStr = restTemplate.getForEntity(javaFrameworks, String.class).getBody();
		JsonElement jsonElement = JsonParser.parseString(responseStr).getAsJsonObject().get("items");
		List<GithubResponseItem> itemList = retrieveListOfItems(jsonElement);
		List<JavaFramework> javaFrameworks = mapItems(itemList);

		sortJavaFrameworks(javaFrameworks, metric);
		
		return javaFrameworks;

	}

	private List<GithubResponseItem> retrieveListOfItems(JsonElement jsonElement) {
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		List<GithubResponseItem> itemList = new ArrayList<>();
		
		if (jsonElement.isJsonObject()) {
			// The returned list has only 1 element
			GithubResponseItem item = gson.fromJson(jsonElement, GithubResponseItem.class);
			itemList.add(item);
		} else if (jsonElement.isJsonArray()) {
			// The returned list has more than 1 element
			Type projectListType = new TypeToken<List<GithubResponseItem>>() {
			}.getType();
			itemList = gson.fromJson(jsonElement, projectListType);
		}

		return itemList;
	}

	private List<JavaFramework> mapItems(List<GithubResponseItem> itemList) {
		List<JavaFramework> javaFrameworks = new ArrayList<>();

		for (GithubResponseItem item : itemList) {
			JavaFramework javaFramework = new JavaFramework();
			javaFramework.setName(item.getName());
			javaFramework.setDescription(item.getDescription());
			javaFramework.setLicenceName(item.getLicense().getName());
			javaFramework.setStarCount(item.getStarCount());
			javaFramework.setApiUrl(item.getApiUrl());
			javaFramework.setHtmlUrl(item.getHtmlUrl());
			javaFramework.setNumOfContr(retrieveNumberOfContr(item.getContrUrl()));
			
			javaFrameworks.add(javaFramework);
		}

		return javaFrameworks;
	}

	private Integer retrieveNumberOfContr(String contrApiUrl) {
		ResponseEntity<String> response = restTemplate.getForEntity(
				contrApiUrl + "?per_page=1&anon=true", String.class);
		
		List<String> links = response.getHeaders().get("link");
		String numberOfContrStr = links.get(0).split(",")[1].split("page")[2].split("=")[1].split(">")[0];
		
		Integer numberOfContr = Integer.valueOf(numberOfContrStr);
		
		return numberOfContr;

	}
	
	private void sortJavaFrameworks(List<JavaFramework> javaFrameworks, String metric) {
		
		Comparator<JavaFramework> comparatorByNumOfContr = (JavaFramework jf1, JavaFramework jf2) -> 
        jf1.getNumOfContr().compareTo( jf2.getNumOfContr());
		
        Comparator<JavaFramework> comparatorByStarCount = (JavaFramework jf1, JavaFramework jf2) -> 
        jf1.getStarCount().compareTo( jf2.getStarCount());
        
		if(KeyMetrics.NUM_OF_CONTR.getName().equals(metric.toLowerCase())) {
			Collections.sort(javaFrameworks, comparatorByNumOfContr.reversed());
		} else if(KeyMetrics.STAR_COUNT.getName().equals(metric.toLowerCase())) {
			Collections.sort(javaFrameworks, comparatorByStarCount.reversed());
		}
		
	}
	

}
