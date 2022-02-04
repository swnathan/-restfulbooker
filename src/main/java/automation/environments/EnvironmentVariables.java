package automation.environments;

import net.thucydides.core.util.SystemEnvironmentVariables;

public class EnvironmentVariables {

	static net.thucydides.core.util.EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();

	public String getRestFullBookerEndPoint() {
		return variables.getProperty("REST_FULL_BOOKER_ENDPOINT");
	}


	
}