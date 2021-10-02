package com.swapi.test;

import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.swapi.ReuseAbleMethod;
import com.swapi.util.XlsReader;
import io.restassured.path.json.JsonPath;

public class IndividualTest extends TestBase {

	private Logger log = Logger.getLogger(IndividualTest.class);
	String response = TestBase.base();

	@Test
	public void verifyPeopleHeightMoreThan200NameMatches() {

		/*
		 * Data will be written and read from an excel sheet I created two column to
		 * input records for Name and age
		 */
		XlsReader reader = new XlsReader(System.getProperty("user.dir") + "//src//main//resources//Book2.xlsx");

		// Global variables and String objects
		String sheetName = "sheet1";
		String column1 = "Name";
		String column2 = "Height";

		String path = null;
		String name = "";
		int height = 0;

		List<String> tallPerson = new ArrayList<>(); // An arrayList to store People above 200cm

		/*
		 * An arrayList containing the expected result of individuals above height 200cm
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

			String res1 = given().log().all().queryParam("page", pageN).when().get("people").then().log().all()
					.assertThat().statusCode(200).extract().response().asString();

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
			log.info("Row number from excel === " + num);

			try {
				Double allHeight = Double.parseDouble(heightNum);
				if (allHeight > 200) {
					counter = counter + 1;
					System.out.println("Counter ==== " + counter);
					log.info("Row number height from excel === " + allHeight);
					String allName = reader.getCellData(sheetName, column1, num);
					String actualName = allName;
					log.info("Namessss === " + actualName);
					tallPerson.add(actualName);
				}
			} catch (NumberFormatException e) {
				log.info(e);
			}

		}
		// verify name matches for names with height 200cm and above
		Assert.assertEquals(tallPerson.get(0), expectedIndividuals.get(0));
		Assert.assertEquals(tallPerson.get(1), expectedIndividuals.get(1));
		Assert.assertEquals(tallPerson.get(2), expectedIndividuals.get(2));
		Assert.assertEquals(tallPerson.get(3), expectedIndividuals.get(3));
		Assert.assertEquals(tallPerson.get(4), expectedIndividuals.get(4));
		Assert.assertEquals(tallPerson.get(5), expectedIndividuals.get(5));
		Assert.assertEquals(tallPerson.get(6), expectedIndividuals.get(6));
		Assert.assertEquals(tallPerson.get(7), expectedIndividuals.get(7));
		Assert.assertEquals(tallPerson.get(8), expectedIndividuals.get(8));
		Assert.assertEquals(tallPerson.get(9), expectedIndividuals.get(9));

	}
}
