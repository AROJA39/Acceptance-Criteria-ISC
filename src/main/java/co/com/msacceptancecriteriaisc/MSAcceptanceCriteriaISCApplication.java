package co.com.msacceptancecriteriaisc;

import co.com.msacceptancecriteriaisc.constants.Constants;
import co.com.msacceptancecriteriaisc.handler.get;
import io.javalin.Javalin;

/**
 * Main class in charge of the initialization of the service where the start port is configured, 
 * the request path and the request handler class is called.
 * @author  
 */
public class MSAcceptanceCriteriaISCApplication {

	/**
     * Method to manage the initialization port configuration, path configuration and redirection to the handler class.
     * 
     * @author Migration Team
     * @param Context
     * @throws Exception
     *
     */
	public static void main(String[] args) {
		try {
			Javalin app = Javalin.create().start(Constants.SERVERPORT);
			app.get("/criteriaISC", new get());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
