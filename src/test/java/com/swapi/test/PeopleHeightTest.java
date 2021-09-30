package com.swapi.test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.swapi.ReuseAbleMethod;
import com.swapi.model.People;
import io.restassured.path.json.JsonPath;

public class PeopleHeightTest extends TestBase {

	String response = TestBase.base();
	private Logger log = Logger.getLogger(PeopleHeightTest.class);
	
	@Test
	public void verifyHeight() {
	
		// An arrayList to store People objects
		List<People> person = new ArrayList<>();
					
		List<String> tallPerson = new ArrayList<>();   // An arrayList to store People above 200cm
					
		// variable and objects
		String path = null;
		String name = "";
		int height = 0;
	
	
			
			JsonPath js = ReuseAbleMethod.rawToJson(response); 	// convert response string to get the json path
			int resultSize = js.get("results.size()");		// get the size of results object
			path = js.getString("next");		// get next page 
			
			for(int i = 0; i < resultSize; i++) {
				name = js.getString("results["+i+"].name");		// get all the names
				
				// use try catch block to catch NumberFormatException
				try {
					height = js.getInt("results["+i+"].height");	// get all the heights
				}catch(NumberFormatException e) {		
					e.printStackTrace();
				}
				// create a People object and pass in name and height to the constructor
				People people = new People(name, height);
				person.add(people);		//store people object in person arrayList
			}
			
			/* if path is not null then it contains a next page
			 * use while loop if path is not null
			 */
			
			while(path != null) {
				
				int pathLastIndex = path.length()-1;	// get the last index of the path which contains the query param
				
				String number = path.substring(pathLastIndex);		// get the last index string value
				
				Integer pageN = Integer.parseInt(number);		// convert string to Integer
				
				/* query the api using the queryParam(). key is page value is pageN
				 * use given, when, then
				 * get response as string
				 */
				
				String res1 = given().log().all().queryParam("page", pageN)
						.when().get("people")
				.then().log().all().assertThat()
				.statusCode(200)
				.extract().response().asString();
				
				
				JsonPath js1 = ReuseAbleMethod.rawToJson(res1); 
				int resultSize1 = js1.getInt("results.size()");		// get the results size
				
				path = js1.getString("next");		// get the object matching the path
				
				for(int i = 0; i < resultSize1; i++) {
					
					name = js1.getString("results["+i+"].name");	// get the name from the results object
					
					// use try catch block to catch NumberFormatException	
					try {
						height = js1.getInt("results["+i+"].height");		//get the height
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					//create a People object and pass in name and height to the constructor
					People people = new People(name, height);
					person.add(people);		// store people object to person arrayList
			}
				
			}
			
			
			int count = 0;
			for(People i: person) {		// ForEach loop to iterate through the person arrayList

				if(i.getHeight() > 200) {		// only enter the if block if condition is met
					log.info(i.getName() + ":" + i.getHeight());		
					name = i.getName();
					tallPerson.add(name);		// add name to tallPerson arrayList
					count++;		//increment count
				}
				
			}
			log.info("Count== " + count);
			assertEquals(count, 10);	// check if actual is equal to expected
	}
}
