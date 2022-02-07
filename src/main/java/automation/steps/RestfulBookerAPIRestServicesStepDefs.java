package automation.steps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.logging.LoggingHandler;
import com.github.javafaker.Faker;
import automation.enums.Fields;
import automation.enums.TokenTypes;
import automation.environments.EnvironmentVariables;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

public class RestfulBookerAPIRestServicesStepDefs {

	EnvironmentVariables EnvironmentVariables = new EnvironmentVariables();
	Faker faker = new Faker();
	Logger logger = Logger.getLogger(LoggingHandler.class.getName());

	private static String bookingID;
	private static String firstName;
	private static String lastName;
	private static String checkINDate;
	private static String checkoutDate;
	private static int bookingAmount;
	HashMap<String, String> dates = new HashMap<String, String>();

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
		JSONObject jsonObject = new JSONObject(responseBody.asString());
		String accessToken = jsonObject.getString("token").toString();
		String tokenAccess = (String) Serenity.getCurrentSession().put("AccessToken", accessToken);
		logger.info(tokenAccess);

	}

	@Then("^Call Get GetBookingIds$")
	public void getBookingsID() {
		Response response = SerenityRest.given().header("Content-Type", "application/json")
				.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
		Assert.assertEquals("Status Code for Get BookingIds Call is different", 200, response.getStatusCode());
	}

	@Then("^Call DeleteBooking for (.*)$")
	public void deleteBooking(TokenTypes TokenTypes) {
		String tokenAccess = (String) Serenity.getCurrentSession().get("AccessToken");
		Response response;
		switch(TokenTypes) {
		case valid:
	     response = SerenityRest.given().header("Content-Type", "application/json")
				.header("Cookie", "token=" + "" + tokenAccess)
				.delete(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
		Assert.assertEquals("Status Code for Delete Booking with Valid Token is different", 201, response.getStatusCode());
		break;
		case invalid:
		     response = SerenityRest.given().header("Content-Type", "application/json")
					.header("Cookie", "token=" + "" + tokenAccess + "test")
					.delete(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
			Assert.assertEquals("Status Code for Delete Booking  with Invalid Token is different", 403, response.getStatusCode());
		break;
	}
	}

	@Then("^Call Get All GetBookingIds$")
	public void getAllBookingsID() {
		Response response = SerenityRest.given().header("Content-Type", "application/json")
				.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/");
		Assert.assertEquals("Status Code for Get BookingIds Call is different", 200, response.getStatusCode());
		JsonPath jsonPathEvaluator = response.jsonPath();
		List<Integer> bookingIds;
		bookingIds = jsonPathEvaluator.getList("bookingid");
		for (int i = 0; i < bookingIds.size(); i++) {
			if (bookingIds.get(i).equals(bookingID)) {
				Assert.assertEquals("Booking Id is different", bookingID, bookingIds.get(i));
			}
		}
	}

	@Then("^Search Booking by (.*)$")
	public void searchBooking(Fields fields) {
		Response response;
		JsonPath jsonPathEvaluator;
		List<Integer> BookingID;
		switch (fields) {
		case firstname:
			response = SerenityRest.given().header("Content-Type", "application/json")
					.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking?firstname=" + "" + firstName);
			Assert.assertEquals("Status Code for Get Search by Booking FirstName is different", 200,
					response.getStatusCode());
			jsonPathEvaluator = response.jsonPath();
			BookingID = jsonPathEvaluator.getList("bookingid");
			for (int i = 0; i < BookingID.size(); i++) {
				if (BookingID.get(i).equals(bookingID)) {
					Assert.assertEquals("Booking Id is different", bookingID, BookingID.get(i));
				}
			}
			break;
		case lastname:
			response = SerenityRest.given().header("Content-Type", "application/json")
					.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking?lastname=" + "" + lastName);
			Assert.assertEquals("Status Code for Get Search by Booking LastName is different", 200,
					response.getStatusCode());
			jsonPathEvaluator = response.jsonPath();
			BookingID = jsonPathEvaluator.getList("bookingid");
			for (int i = 0; i < BookingID.size(); i++) {
				if (BookingID.get(i).equals(bookingID)) {
					Assert.assertEquals("Booking Id is different", bookingID, BookingID.get(i));
				}
			}
			break;
		case name:
			response = SerenityRest.given().header("Content-Type", "application/json")
					.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking?firstname=" + "" + firstName + "&"
							+ "" + "lastname=" + "" + lastName);
			Assert.assertEquals("Status Code for Get Search by Booking FirstName&LastName is different", 200,
					response.getStatusCode());
			jsonPathEvaluator = response.jsonPath();
			BookingID = jsonPathEvaluator.getList("bookingid");
			for (int i = 0; i < BookingID.size(); i++) {
				if (BookingID.get(i).equals(bookingID)) {
					Assert.assertEquals("Booking Id is different", bookingID, BookingID.get(i));
				}
			}
			break;
		case Dates:
			response = SerenityRest.given().header("Content-Type", "application/json")
					.get(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking?checkin=" + "" + checkINDate + "&"
							+ "" + "checkout=" + "" + checkoutDate);
			Assert.assertEquals("Status Code for Get Search by Booking FirstName&LastName is different", 200,
					response.getStatusCode());
			jsonPathEvaluator = response.jsonPath();
			BookingID = jsonPathEvaluator.getList("bookingid");
			for (int i = 0; i < BookingID.size(); i++) {
				if (BookingID.get(i).equals(bookingID)) {
					Assert.assertEquals("Booking Id is different", bookingID, BookingID.get(i));
				}
			}
			break;
		}
	}

	@Then("^Call POST CreateBooking$")
	public void postCreateBooking() throws ParseException {
		Faker faker = new Faker();
		Map map = new HashMap<String, Map<String, String>>();
		Map<String, String> bookingResponse = new HashMap<String, String>();
		Map<String, String> bookingDate = new HashMap<String, String>();
		JSONParser jsonParser = new JSONParser();
		ResponseBody ResponseBody = null;
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String checkInDate = sdf.format(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, 3);
		String checkOutDate = sdf.format(cal.getTime());
		JSONObject json = new JSONObject();
		json.put("firstname", faker.name().firstName());
		json.put("lastname", faker.name().lastName());
		json.put("totalprice", faker.number().digits(4));
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
		bookingResponse = response.jsonPath().getMap("booking");
		bookingDate = (Map<String, String>) response.jsonPath().getMap("booking").get("bookingdates");
		bookingID = jsonPathEvaluator.getString("bookingid");
		firstName = bookingResponse.get("firstname");
		lastName = bookingResponse.get("lastname");
		bookingAmount = json.getInt("totalprice");
		checkINDate = bookingDate.get("checkin");
		checkoutDate = bookingDate.get("checkout");
		Assert.assertEquals("First Name Displayed in Post Create Booking Response is different", json.get("firstname"),
				firstName);
		Assert.assertEquals("Last Name Displayed in Post Create Booking Response is different", json.get("lastname"),
				lastName);
		Assert.assertEquals("Booking Checkin Dates in Post Create Booking Response is different",
				bookingDates.get("checkin"), bookingDate.get("checkin"));
		Assert.assertEquals("Booking Checkout Dates in Post Create Booking Response is different",
				bookingDates.get("checkout"), bookingDate.get("checkout"));
		Assert.assertEquals("Booking Amount in Post Create Booking Response is different", json.getInt("totalprice"),
				bookingResponse.get("totalprice"));
		Assert.assertEquals("Additional Needs in Post Create Booking Response is different",
				json.get("additionalneeds"), bookingResponse.get("additionalneeds"));

	}

	@Then("^Call UpdateBookings for (.*)$")
	public void updateBookings(TokenTypes TokenTypes) {
		JSONObject json = new JSONObject();
		JSONObject bookingDates = new JSONObject();
		String tokenAccess = (String) Serenity.getCurrentSession().get("AccessToken");
		Response response;
		Map<String, String> bookingResponse = new HashMap<String, String>();
		JsonPath jsonPathEvaluator;
		switch (TokenTypes) {
		case valid:
			json.put("firstname", firstName);
			json.put("lastname", lastName);
			json.put("totalprice", bookingAmount);
			bookingDates.put("checkin", checkINDate);
			bookingDates.put("checkout", checkoutDate);
			json.put("bookingdates", bookingDates);
			json.put("additionalneeds", "Lunch Included");
			json.put("depositpaid", false);
			response = SerenityRest.given().header("Content-Type", "application/json")
					.header("Cookie", "token=" + "" + tokenAccess).body(json.toString())
					.put(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
			jsonPathEvaluator = response.jsonPath();
			bookingResponse = response.jsonPath().getMap("booking");
			Assert.assertEquals("Status Code for Update Booking Call is different", 200, response.getStatusCode());
			Assert.assertEquals("First Name Displayed in Update Booking Call Response is different",
					json.get("firstname"), jsonPathEvaluator.get("firstname"));
			Assert.assertEquals("Last Name Displayed in Update Booking Call Response is different",
					json.get("lastname"), jsonPathEvaluator.get("lastname"));
			Assert.assertEquals("Booking Checkin Dates in Update Booking Call Response is different",
					bookingDates.get("checkin"), checkINDate);
			Assert.assertEquals("Booking Checkout Dates in Update Booking Call Response is different",
					bookingDates.get("checkout"), checkoutDate);
			Assert.assertEquals("Booking Amount in Post Update Booking Call Response is different",
					json.get("totalprice"), jsonPathEvaluator.get("totalprice"));
			Assert.assertEquals("Additional Needs in Update Booking Call Response is different",
					json.get("additionalneeds"), jsonPathEvaluator.get("additionalneeds"));

			break;
		case invalid:
			json.put("firstname", firstName);
			json.put("lastname", lastName);
			json.put("totalprice", bookingAmount);
			bookingDates.put("checkin", checkINDate);
			bookingDates.put("checkout", checkoutDate);
			json.put("bookingdates", bookingDates);
			json.put("additionalneeds", "Lunch Included");
			json.put("depositpaid", false);
			response = SerenityRest.given().header("Content-Type", "application/json")
					.header("Cookie", "token=" + "" + tokenAccess + "test").body(json.toString())
					.put(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
			jsonPathEvaluator = response.jsonPath();
			Assert.assertEquals("Status Code for Update Booking with Invalid Token Call is different", 403,
					response.getStatusCode());
			break;
		}
	}

	@Then("^Call PartialUpdateBookings for (.*)$")
	public void partialUpdateBookings(TokenTypes TokenTypes) {
		JSONObject json = new JSONObject();
		Map<String, String> bookingDate = new HashMap<String, String>();
		String tokenAccess = (String) Serenity.getCurrentSession().get("AccessToken");
		Response response;
		JsonPath jsonPathEvaluator;
		Map<String, String> bookingResponse = new HashMap<String, String>();

		switch (TokenTypes) {
		case valid:
			json.put("additionalneeds", "Break Fast,Lunch,Dinner Included");
			response = SerenityRest.given().header("Content-Type", "application/json")
					.header("Cookie", "token=" + "" + tokenAccess).body(json.toString())
					.patch(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
			jsonPathEvaluator = response.jsonPath();
			bookingDate = (Map<String, String>) response.jsonPath().get("bookingdates");
			Assert.assertEquals("Status Code for Partial Update Booking is different", 200, response.getStatusCode());
			Assert.assertEquals("First Name Displayed in Partial Update Booking Response is different", firstName,
					jsonPathEvaluator.get("firstname"));
			Assert.assertEquals("Last Name Displayed in Partial Update Booking Response is different", lastName,
					jsonPathEvaluator.get("lastname"));
			Assert.assertEquals("Booking Checkin Dates in Partial Update Booking Response is different", checkINDate,
					bookingDate.get("checkin"));
			Assert.assertEquals("Booking Checkout Dates in Partial Update Booking Response is different", checkoutDate,
					bookingDate.get("checkout"));
			Assert.assertEquals("Additional Needs in Partial Update Booking Response is different",
					json.get("additionalneeds"), jsonPathEvaluator.get("additionalneeds"));

			break;
		case invalid:
			json.put("additionalneeds", "Break Fast,Lunch,Dinner Included");
			response = SerenityRest.given().header("Content-Type", "application/json")
					.header("Cookie", "token=" + "" + tokenAccess + "test").body(json.toString())
					.patch(EnvironmentVariables.getRestFullBookerEndPoint() + "/booking/" + "" + bookingID);
			jsonPathEvaluator = response.jsonPath();
			Assert.assertEquals("Status Code for Partial Booking with Invalid Token Call is different", 403,
					response.getStatusCode());
		}
	}
}
