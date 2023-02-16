package co.com.msacceptancecriteriaisc.bussines;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import co.com.msacceptancecriteriaisc.constants.Constants;
import co.com.msacceptancecriteriaisc.extraction.ExtractionField;
import co.com.msacceptancecriteriaisc.objects.TrackingAcceptanceCriterial;
import co.com.msacceptancecriteriaisc.support.ClientFileUpload;

/**
 * The class manages the logic of the acceptance criteria for ISC type transactions, 
 * where you have a method that calls the ISC type data extraction method or 
 * calls the control method to check the status of the last transaction sent.
 * 
 * @author Migration Team
 */
public class AcceptanceCriteriaISCBusiness {

	public static JSONObject tracking = null;
	public static JSONObject jsonResponse = null;

	/**
	 * Invoking the method to consume the file service
	 * 
	 * @author Migration Team
	 * @param String
	 * @return ConcurrentHasMap
	 */

	public static ConcurrentHashMap<String, Object> acceptanceCriterialIsc = (ConcurrentHashMap<String, Object>) new ClientFileUpload()
			.getDataFile(Constants.ACCEPTANCECRITERIALISC);

	/**
	 * Method that receives the parameters of the request, validates what type of
	 * operation it is and sends it to the method that consumes it.
	 * 
	 * @author Migration Team
	 * @param HasMap<String, String>
	 * @return JSONObject
	 *
	 */
	public String getOperation(HashMap<String, String> request) throws Exception {
		jsonResponse = new JSONObject();
		if (Objects.isNull(acceptanceCriterialIsc)) {
			jsonResponse.put(Constants.ERROR, "Los componentes no están completos para procesar la información.");
			jsonResponse.put(Constants.STATUS, Constants.FAILED);
			jsonResponse = new TrackingAcceptanceCriterial().getInfoAcceptance(jsonResponse);
			return jsonResponse.toString();
		}
		if (request.containsKey(Constants.RULEREQUEST)) {
			jsonResponse = (JSONObject) extraccionAcceptanceCriteria(request);
		} else if (request.containsKey(Constants.STATUS)) {
			jsonResponse = new TrackingAcceptanceCriterial().getInfoAcceptance(tracking);
		} else {
			jsonResponse.put(Constants.ERROR, Constants.OPERATION_NOT_FOUND);
			jsonResponse.put(Constants.STATUS, Constants.FAILED);
			jsonResponse = new TrackingAcceptanceCriterial().getInfoAcceptance(jsonResponse);

		}
		return jsonResponse.toString();

	}

	/**
	 * Method of control for the identification of the criteria of acceptance of the transaction where a grouping 
	 * is made in fixed, calculated, required and input parameters, are added to an object that obtains the groups 
	 * and a call is made to the method of extraction of the information. 
	 * 
	 * @author Migration Team
	 * @param HasMap<String, String>
	 * @return JSONObject
	 *
	 */
	@SuppressWarnings("unchecked")
	private JSONObject extraccionAcceptanceCriteria(HashMap<String, String> request) {
		JSONObject responseInfo = null;
		String rule = request.get(Constants.RULEREQUEST);
		HashMap<String, Object> dataCriteria = (HashMap<String, Object>) acceptanceCriterialIsc.get(rule);
		tracking = new JSONObject();
		Set<String> keyRequest = request.keySet();
		keyRequest.remove(Constants.RULEREQUEST);
		HashMap<String, Object> inputCriteria = new HashMap<>();
		HashMap<String, Object> fixedCriteria = new HashMap<>();
		HashMap<String, Object> calculateCriteria = new HashMap<>();
		HashMap<String, Object> requestCriteria = new HashMap<>();
		
		dataCriteria.keySet().removeIf(t -> t.contains(Constants.DESCRIPTION));
		for (String criteria : dataCriteria.keySet()) {
			if (criteria.contains(Constants.INPUT)) {
				inputCriteria.put(criteria, dataCriteria.get(criteria));
				for (String key : keyRequest) {
					String string = key.concat(Constants.DELIMITER + Constants.INPUT);
					requestCriteria.put(string, request.get(key));
				}
			}else if(criteria.contains(Constants.FIXED)) {
				fixedCriteria.put(criteria, dataCriteria.get(criteria));
			}else if(criteria.contains(Constants.CALCULATE)) {
				calculateCriteria.put(criteria, dataCriteria.get(criteria));
			}
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put(Constants.INPUTS, inputCriteria);
		map.put(Constants.FIXED, fixedCriteria);
		map.put(Constants.CALCULATE, calculateCriteria);
		map.put(Constants.REQUEST, requestCriteria);
		responseInfo = new JSONObject(new ExtractionField().extractionFields(map));
		return responseInfo;
	}
	
}
