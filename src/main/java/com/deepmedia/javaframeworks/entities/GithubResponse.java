package com.deepmedia.javaframeworks.entities;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GithubResponse {
	@Expose @SerializedName("total_count")
	private Integer count;
	
	@SerializedName("incomplete_results")
	private Boolean result;
	
	@Expose @SerializedName("items")
	private List<GithubResponseItem> items;
}
