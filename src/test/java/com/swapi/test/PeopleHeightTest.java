package com.swapi.test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import com.swapi.ReuseAbleMethod;
import com.swapi.util.XlsReader;

import io.restassured.path.json.JsonPath;

public class PeopleHeightTest extends TestBase {

	private static Logger log = Logger.getLogger(PeopleHeightTest.class);
	String response = TestBase.base();

	@Test
	public void verifyHeight() throws IOException {

		/*
		 * Data will be written and read from an excel sheet I created two column to
		 * input records for Name and age
		 */
		XlsReader reader = new XlsReader(System.getProperty("user.dir") + "//src//main//resources//Book2.xlsx");

		// Excel sheet columns and sheet name
		String sheetName = "sheet1";
		String column1 = "Name";
		String column2 = "Height";

		// variables and String objects
		String path = null;
		String name = "";
		int height = 0;

		// get the value of the property using its key 'resource'
		String resource = properties.getProperty("resource");

		// get the value of the property using its key 'paramName'
		String paramName = properties.getProperty("paramName");

		JsonPath js = ReuseAbleMethod.rawToJson(response); // convert response string to get the json path
		int resultSize = js.get("results.size()"); // get the size of results object
		path = js.getString("next"); // get next page

		int count = 1;
		for (int i = 0; i < resultSize; i++) {
			count = count + 1;
			log.info("Count == " + count);
			name = js.getString("results[" + i + "].name"); // get all the names
			reader.setCellData(sheetName, column1, count, name);
			// use try catch block to catch NumberFormatException
			try {
				log.info("Count == " + count);
				height = js.getInt("results[" + i + "].height"); // get all the heights
				reader.setCellData(sheetName, column2, count, height);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

		}

		/*
		 * if path is not null then it contains a next page use while loop if path is
		 * not null
		 */

		while (path != null) {

			int pathLastIndex = path.length() - 1; // get the last index of the path which contains the query param

			String number = path.substring(pathLastIndex); // get the last index string value
			Integer pageN = Integer.parseInt(number); // convert string to Integer

			/*
			 * query the api using the queryParam(). key is page value is pageN use given,
			 * when, then get response as string
			 */

			String res1 = given().queryParam(paramName, pageN).when().get(resource).then().assertThat().statusCode(200)
					.extract().response().asString();

			JsonPath js1 = ReuseAbleMethod.rawToJson(res1);
			int resultSize1 = js1.getInt("results.size()"); // get the results size

			path = js1.getString("next"); // get the object matching the path

			for (int i = 0; i < resultSize1; i++) {
				count = count + 1;
				log.info("Count == " + count);

				name = js1.getString("results[" + i + "].name"); // get the name from the results object
				reader.setCellData(sheetName, column1, count, name);
				// use try catch block to catch NumberFormatException
				try {
					log.info("Count ==== " + count);
					height = js1.getInt("results[" + i + "].height"); // get the height
					reader.setCellData(sheetName, column2, count, height);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			}

		}

		int counter = 0;
		// get data from excel sheet age column
		for (int num = 2; num <= count; num++) {
			String heightNum = reader.getCellData(sheetName, column2, num);

			try {
				double allHeight = Double.parseDouble(heightNum);
				int allHeight1 = (int) allHeight; // cast double to int
				if (allHeight1 > 200) {
					counter = counter + 1;
				}
			} catch (NumberFormatException e) {
				log.info(e);
			}

		}
		// check if actual number is equal expected number
		int expectedNumber = 10;
		int actualNumber = counter;
		assertEquals(actualNumber, expectedNumber);

	}
}
