package com.swapi.test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.swapi.ReuseAbleMethod;
import com.swapi.model.People;
import io.restassured.path.json.JsonPath;

public class IndividualTest extends TestBase{

	String response = TestBase.base();
	
	@Test
	public void verifyIndividual() {
		
	
	
	// An arrayList to store People objects
			List<People> person = new ArrayList<>();
			
			List<String> tallPerson = new ArrayList<>();   // An arrayList to store People above 200cm
			
			// variable and objects
			String path = null;
			String name = "";
			int height = 0;
			
			/* An arrayList containing the expected result of individuals above height 200cm
			 * This data can be read from an excel sheet as well
			 */
			
			List<String> expectedIndividuals = new ArrayList<>();
			expectedIndividuals.add("Darth Vader");
			expectedIndividuals.add("Chewbacca");
			expectedIndividuals.add("Roos Tarpals");
			expectedIndividuals.add("Rugor Nass");
			expectedIndividuals.add("Yarael Poof");
			expectedIndividuals.add("Lama Su");
			expectedIndividuals.add("Tuan Wu");
			expectedIndividuals.add("Grievous");
			expectedIndividuals.add("Tarfful");
			expectedIndividuals.add("Tion Medon");
			
			
			
					JsonPath js = ReuseAbleMethod.rawToJson(response);  // convert response string to get the json path
					int resultSize = js.get("results.size()");		// get the size of results object
					path = js.getString("next");		// get next page 
					
					for(int i = 0; i < resultSize; i++) {
						name = js.getString("results["+i+"].name");		// get all the names
						
						// use try catch block to catch NumberFormatException
						try {
							height = js.getInt("results["+i+"].height");   // get all the heights
						}catch(NumberFormatException e) {
							e.printStackTrace();
						}
						// create a People object and pass in name and height to the constructor
						People people = new People(name, height);
						person.add(people);		//store people object in person arraylist
					}
					
					/* if path is not null then it contains a next page
					 * use while loop if path is not null
					 */
					
					while(path != null) {
						
						int pathLastIndex = path.length()-1;	// get the last index of the path which contains the query param
						
						String number = path.substring(pathLastIndex);	// get the last index string value
						Integer pageN = Integer.parseInt(number);	// convert string to Integer
					
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
								height = js1.getInt("results["+i+"].height");	//get the height
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							//create a People object and pass in name and height to the constructor
							People people = new People(name, height);
							person.add(people);		// store people object to person arrayList
					}
						
					}
					
					
					for(People i: person) {		// ForEach loop to iterate through the person arrayList
		
						if(i.getHeight() > 200) {		// only enter the if block if condition is met
							name = i.getName();		
							tallPerson.add(name);		// add name to tallPerson arrayList
						}
						
					}
				
					assertEquals(tallPerson, expectedIndividuals);		// check if actual is same as expected
}
}
