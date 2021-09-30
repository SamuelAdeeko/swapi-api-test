package com.swapi.test;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;

public class TestBase {
	
	
	public static String base() {
	
	// validate if add booking API works as expected
	RestAssured.baseURI = "https://swapi.dev/api";
	
	
	String response = given().log().all().get("/people")
	.then().log().all().assertThat()
	.statusCode(200)
	.extract().response().asString();
	//System.out.println("Response === " + response);
	
	return response;
	}
}
