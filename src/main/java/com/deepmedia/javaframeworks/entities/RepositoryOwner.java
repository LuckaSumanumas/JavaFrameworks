package com.deepmedia.javaframeworks.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RepositoryOwner {

	@Expose @SerializedName("login") 
	private String login;

}
