package com.deepmedia.javaframeworks.gateways;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.deepmedia.javaframeworks.entities.GithubResponseItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JavaFrameworksGateway {
	
	private RestTemplate restTemplate;
	
	public JavaFrameworksGateway() {
		restTemplate = new RestTemplate();
	}

	
	public List<GithubResponseItem> retrieveRepoItems(String url) {
		String responseStr = restTemplate.getForEntity(url, String.class).getBody();
		JsonElement jsonElement = JsonParser.parseString(responseStr).getAsJsonObject().get("items");
		List<GithubResponseItem> itemList = retrieveListOfItems(jsonElement);
		
		return itemList;
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
	
	public Integer retrieveNumberOfContr(String url) {
		ResponseEntity<String> response = restTemplate.getForEntity(
				url + "?per_page=1&anon=true", String.class);
		
		List<String> links = response.getHeaders().get("link");
		String numberOfContrStr = links.get(0).split(",")[1].split("page")[2].split("=")[1].split(">")[0];
		
		Integer numberOfContr = Integer.valueOf(numberOfContrStr);
		
		return numberOfContr;

	}
}
