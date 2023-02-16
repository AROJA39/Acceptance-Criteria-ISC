package co.com.msacceptancecriteriaisc.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import co.com.msacceptancecriteriaisc.bussines.AcceptanceCriteriaISCBusiness;
import co.com.msacceptancecriteriaisc.constants.Constants;
import co.com.msacceptancecriteriaisc.objects.TrackingAcceptanceCriterial;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;

/**
 * Class of management of incoming and outgoing requests
 * 
 * @author Migration Team
 */
public class get implements Handler {
	
	private AcceptanceCriteriaISCBusiness business = new AcceptanceCriteriaISCBusiness();

	/**
	 * Method to control the handling of incoming and outgoing requests, where the
	 * extraccionAcceptanceCriterial method is invoked.
	 * 
	 * @author Migration Team
	 * @param Context
	 * @throws Exception
	 *
	 */
	@Override
	public void handle(Context ctx) throws Exception {
		try {
			Map<String, List<String>> params = ctx.queryParamMap();
			HashMap<String, String> requestParams = new HashMap<>();
			params.forEach((key, value) -> requestParams.put(key, value.get(0)));
			ctx.contentType(ContentType.APPLICATION_JSON);
			ctx.status(HttpCode.OK).result(business.getOperation(requestParams));
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject error = new JSONObject();
			error.put(Constants.STATUS, Constants.FAILED);
			error.put(Constants.ERROR, "En este momento no se puede procesar la solicitud");
			error = new TrackingAcceptanceCriterial().getInfoAcceptance(error);
			ctx.status(HttpCode.BAD_REQUEST).result(error.toString());
		}
		
	}

}
