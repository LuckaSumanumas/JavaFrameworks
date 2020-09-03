package com.deepmedia.javaframeworks.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JavaFramework {

	private Integer repoId;
	private String name;
	private String owner;
	private String description;
	private String licenceName;
	private Boolean isUserStarRepo;
	private Integer starCount;
	private Integer numOfContr;
	private String apiUrl;
	private String htmlUrl;
}
