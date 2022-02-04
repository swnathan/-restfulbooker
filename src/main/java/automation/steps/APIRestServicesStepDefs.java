package automation.steps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.logging.LoggingHandler;
import com.github.javafaker.Faker;
import automation.enums.Fields;
import automation.environments.EnvironmentVariables;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

public class APIRestServicesStepDefs {

	EnvironmentVariables EnvironmentVariables = new EnvironmentVariables();
	Faker faker = new Faker();
	Logger logger = Logger.getLogger(LoggingHandler.class.getName());

	private static String bookingID;
	private static String firstName;
	private static String lastName;

	public static String getBookingID() {
		return bookingID;
	}

	@Given("Create Auth Token")
	public void createToken() {
		JSONObject json = new JSONObject();
		ResponseBody responseBody = null;
		json.put("username", "admin");
		json.put("password", "password123");
		Response response = SerenityRest.given().header("Content-Type", "application/json").request()
				.body(json.toString()).post(EnvironmentVariables.getRestFullBookerEndPoint() + "/auth/");
		logger.info(EnvironmentVariables.getRestFullBookerEndPoint() + "/auth/");
		Assert.assertEquals("Status Code for Create Auth Token Call is different", 200, response.getStatusCode());
		responseBody = response.getBody();
		logger.info(responseBody.asString());
		JsonPath jsonPathEvaluator = response.jsonPath();
		JSONObject jsonObject = new JSONObject(responseBody.asString());
		System.out.println(jsonObject.toString());
		System.out.println(jsonObject.getString("token").toString());
		String accessToken = jsonObject.getString("token").toString();
		String tokenAccess = (String) Serenity.getCurrentSession().put("AccessToken", accessToken);
		logger.info(tokenAccess);

	}

	@Then("^Call Get GetBookingIds$")
	public void getBookingsID() {
		Response response = SerenityRest.given().header("Content-Type", "application/json")
				.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
		Assert.assertEquals("Status Code for List of Products Get Call is different", 200, response.getStatusCode());
	}

	@Then("^Call Get All GetBookingIds$")
	public void getAllBookingsID() {
		List<HashMap<String, Integer>> reports1;
		Response response = SerenityRest.given().header("Content-Type", "application/json")
				.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/");
		Assert.assertEquals("Status Code for Get BookingIds Call is different", 200, response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		List<Integer> test;
		test = jsonPathEvaluator.getList("bookingid");
		for (int i = 0; i < test.size(); i++) {
			if (test.get(i).equals(bookingID)) {
				Assert.assertEquals("Booking Id is different", bookingID, test.get(i));
			}
		}
	}

	@Then("^Search Booking by (.*)$")
	public void searchBooking(Fields fields) {
		Response response;
		JsonPath jsonPathEvaluator;
		List<Integer> test;
		switch (fields) {
		case firstname:
			/// booking?firstname=sally&lastname=brown
			response = SerenityRest.given().header("Content-Type", "application/json")
					.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking?firstname=" + "" + firstName);
			Assert.assertEquals("Status Code for Get Search by Booking FirstName is different", 200,
					response.getStatusCode());
			jsonPathEvaluator = response.jsonPath();
			test = jsonPathEvaluator.getList("bookingid");
			for (int i = 0; i < test.size(); i++) {
				if (test.get(i).equals(bookingID)) {
					Assert.assertEquals("Booking Id is different", bookingID, test.get(i));
				}
			}
			break;
		case lastname:
			response = SerenityRest.given().header("Content-Type", "application/json")
					.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking?lastname=" + "" + lastName);
			Assert.assertEquals("Status Code for Get Search by Booking LastName is different", 200,
					response.getStatusCode());
			jsonPathEvaluator = response.jsonPath();
			test = jsonPathEvaluator.getList("bookingid");
			for (int i = 0; i < test.size(); i++) {
				if (test.get(i).equals(bookingID)) {
					Assert.assertEquals("Booking Id is different", bookingID, test.get(i));
				}
			}
			break;
		// ?firstname=sally&lastname=brown
		case name:
			response = SerenityRest.given().header("Content-Type", "application/json")
					.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking?firstname=" + "" + firstName + "&"
							+ "" + "lastname=" + "" + lastName);
			Assert.assertEquals("Status Code for Get Search by Booking FirstName&LastName is different", 200,
					response.getStatusCode());
			jsonPathEvaluator = response.jsonPath();
			test = jsonPathEvaluator.getList("bookingid");
			for (int i = 0; i < test.size(); i++) {
				if (test.get(i).equals(bookingID)) {
					Assert.assertEquals("Booking Id is different", bookingID, test.get(i));
				}
			}
			break;
		}
	}

	@Then("^Call POST CreateBooking$")
	public void postCreateBooking() {
		Faker faker = new Faker();
		List<HashMap<String, String>> reports1;
		Map<String, String> row = new HashMap<String, String>();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String checkInDate = sdf.format(cal.getTime());
		System.out.println(sdf.format(cal.getTime()) + " is the date before adding days");
		cal.add(Calendar.DAY_OF_MONTH, 3);
		String checkOutDate = sdf.format(cal.getTime());
		JSONObject json = new JSONObject();
		json.put("firstname", faker.name().firstName());
		json.put("lastname", faker.name().lastName());
		json.put("totalprice", faker.commerce().price());
		json.put("depositpaid", false);
		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", checkInDate);
		bookingDates.put("checkout", checkOutDate);
		json.put("bookingdates", bookingDates);
		json.put("additionalneeds", "Breakfast");
		System.out.println(json.toString());
		Response response = SerenityRest.given().header("Content-Type", "application/json").request()
				.body(json.toString()).post(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/");
		JsonPath jsonPathEvaluator = response.jsonPath();
		Assert.assertEquals("Status Code for Post Create Booking is different", 200, response.getStatusCode());
		int id = jsonPathEvaluator.getInt("bookingid");
		System.out.println(id);
		row = response.jsonPath().getMap("booking");
		System.out.println(row.get("firstname"));
		bookingID = jsonPathEvaluator.getString("bookingid");
		firstName = row.get("firstname");
		lastName = row.get("lastname");
		Assert.assertEquals("First Name Displayed in Post Create Booking Response is different", json.get("firstname"),
				row.get("firstname"));
		Assert.assertEquals("Last Name Displayed in Post Create Booking Response is different", json.get("lastname"),
				row.get("lastname"));

	}

}
