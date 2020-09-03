package com.deepmedia.javaframeworks.entities;

import lombok.Data;

@Data
public class UserAuth {
	private String username;
	private String password;
	private String token;
	
	public UserAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
