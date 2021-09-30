package com.swapi.test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.swapi.ReuseAbleMethod;
import com.swapi.model.People;
import io.restassured.path.json.JsonPath;

public class PeopleTotalTest {

	String response = TestBase.base();
	
@Test
	
	public void findTotalPeople() {
		
		
		List<People> person = new ArrayList<>();
		
		
		String path = null;
		String name = "";
		int height = 0;
		
				
				JsonPath js = ReuseAbleMethod.rawToJson(response); 
				int resultSize = js.get("results.size()");
				path = js.getString("next");
				
				for(int i = 0; i < resultSize; i++) {
					name = js.getString("results["+i+"].name");
					try {
						height = js.getInt("results["+i+"].height");
					}catch(NumberFormatException e) {
						e.printStackTrace();
					}
					People people = new People(name, height);
					person.add(people);
				}
				
				
				//int pageN = 0;
				
				
				while(path != null) {
					
					int pathLastIndex = path.length()-1;
					
					String number = path.substring(pathLastIndex);
					System.out.println("Query number = " + number);
					Integer pageN = Integer.parseInt(number);
					System.out.println("Page number = " + pageN);
					String res1 = given().log().all().queryParam("page", pageN)
							.when().get("people")
					.then().log().all().assertThat()
					.statusCode(200)
					.extract().response().asString();
					System.out.println("Response === " + res1);
					
					JsonPath js1 = ReuseAbleMethod.rawToJson(res1); 
					int resultSize1 = js1.getInt("results.size()");
					
					path = js1.getString("next");
					
					for(int i = 0; i < resultSize1; i++) {
						
						name = js1.getString("results["+i+"].name");
						
						try {
							height = js1.getInt("results["+i+"].height");
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
						
						People people = new People(name, height);
						person.add(people);
				}
					
				}
			int totalNumber =	person.size();
			int expectedNumber = 82;
			assertEquals(totalNumber, expectedNumber);
}
}
