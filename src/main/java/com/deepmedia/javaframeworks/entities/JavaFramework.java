package com.deepmedia.javaframeworks.entities;

import lombok.Data;

@Data
public class JavaFramework {

	private String name;
	private String description;
	private String licenceName;
	private Boolean isUserStarRepo;
	private Integer starCount;
	private Integer numOfContr;
	private String apiUrl;
	private String htmlUrl;
}
