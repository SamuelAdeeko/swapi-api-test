package com.swapi.test;

import static io.restassured.RestAssured.given;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.Test;
import io.restassured.RestAssured;

public class SwapiEndPointTest extends TestBase {

	@Test
	public void endPointValidation() {

		Properties properties = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream("./src/main/resources/testdata.properties");
			properties.load(inputStream);
		} catch (IOException e) {
			log.info(e);
		}

		// get the value of the property using its key 'uri'
		String uri = properties.getProperty("uri");

		// get the value of the property using its key 'resource'
		String resource = properties.getProperty("resource");

		RestAssured.baseURI = uri;

		// validate if add booking API works as expected
		given().get(resource).then().assertThat().statusCode(200).extract().response().asString();

	}

}