package com.swapi.test;

import static io.restassured.RestAssured.given;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class SwapiEndPointTest {

	
	private Logger log = Logger.getLogger(SwapiEndPointTest.class);

	@Test
	public void endPointValidation() {
	
	// validate if add booking API works as expected
	RestAssured.baseURI = "https://swapi.dev/api";
	
	
	String response = given().log().all().get("/people")
	.then().log().all().assertThat()
	.statusCode(200)
	.extract().response().asString();
	
	log.info("Response " + response);
	}

}