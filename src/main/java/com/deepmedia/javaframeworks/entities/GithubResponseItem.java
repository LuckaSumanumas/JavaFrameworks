package com.deepmedia.javaframeworks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GithubResponseItem {
	@Expose @SerializedName("name") 
	private String name;
	
	@Expose @SerializedName("description") 
	private String description;
	
	@Expose @SerializedName("license") 
	private RepositoryLicense license;
	
	@Expose @SerializedName("stargazers_count") 
	private Integer starCount;
	
	@Expose @SerializedName("url") 
	private String apiUrl;
	
	@Expose @SerializedName("html_url") 
	private String htmlUrl;
	
	@Expose @SerializedName("contributors_url") 
	private String contrUrl;
	
}
