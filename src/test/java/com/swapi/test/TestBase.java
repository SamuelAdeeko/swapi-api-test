package com.swapi.test;

import static io.restassured.RestAssured.given;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import io.restassured.RestAssured;

public class TestBase {

	static Logger log = Logger.getLogger(TestBase.class);
	public static Properties properties = new Properties();

	public static String base() {

		try {
			FileInputStream inputStream = new FileInputStream("./src/main/resources/testdata.properties");
			properties.load(inputStream);
		} catch (IOException e) {
			log.info(e);
		}
		// get the value of the property using its key 'uri'
		String uri = properties.getProperty("uri");

		// get the value of the property using its key 'uri'
		String resource = properties.getProperty("resource");

		RestAssured.baseURI = uri;

		String response = given().when().get(resource).then().assertThat().statusCode(200).extract().response()
				.asString();

		return response;
	}
}
