package com.swapi.model;

public class People {

	
	String name;
	int height;
	

	public People(String name, int height) {
		super();
		this.name = name;
		this.height = height;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}
	
}
