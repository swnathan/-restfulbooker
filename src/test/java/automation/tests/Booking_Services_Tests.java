package automation.tests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class) 	
@CucumberOptions(tags= {" @test"},features = "src/test/resources/APITests.feature",
glue = "automation.steps")
public class Booking_Services_Tests {

}
