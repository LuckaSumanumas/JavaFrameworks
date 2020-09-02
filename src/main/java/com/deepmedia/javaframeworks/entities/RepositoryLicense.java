package com.deepmedia.javaframeworks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RepositoryLicense {
	@Expose @SerializedName("name") 
	private String name;
}
