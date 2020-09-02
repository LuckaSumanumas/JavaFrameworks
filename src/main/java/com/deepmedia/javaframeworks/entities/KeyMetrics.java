package com.deepmedia.javaframeworks.entities;

public enum KeyMetrics {
	STAR_COUNT("starcount"),
	NUM_OF_CONTR("numofcontr");
	
	private String name;
	
	KeyMetrics(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
