package co.com.msacceptancecriteriaisc.extraction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import co.com.msacceptancecriteriaisc.constants.Constants;
import co.com.msacceptancecriteriaisc.support.CharacterEncodingConversion;

/**
 * Class in charge of the extraction of fixed fields, calculated, 
 * and that require a transformation of the information, 
 * also calls are made to a class for the transformation to EBCDIC.
 * @author Migration Team
 */
public class ExtractionField {

	private HashMap<String, Object> responseExtraction = new HashMap<>();
	private StringBuilder msg_isc = new StringBuilder();
	private StringBuilder msg = new StringBuilder();
	private String data, convert = null;

	
	
	/**
     * Method that calls independent extraction and transformation methods for different fields such as fixed, 
     * calculated, inputs and required.
     * 
     * @author Migration Team
     * @param HasMap<String, Object>
     * @return HasMap<String, Object>
     *
     */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> extractionFields(HashMap<String, Object> request) {
		responseExtraction = new HashMap<>();
		responseExtraction = extractionFixedField((HashMap<String, Object>) request.get(Constants.FIXED));
		responseExtraction = extractionCalculateField((HashMap<String, Object>) request.get(Constants.CALCULATE));
		responseExtraction = extractionInputFields((HashMap<String, Object>) request.get(Constants.INPUTS),
				(HashMap<String, Object>) request.get(Constants.REQUEST));
		responseExtraction.put("MSG_ISC", msg_isc);
		responseExtraction.put("MSG", msg);
		return responseExtraction;
	}

	
	/**
     * Method in charge of required fields and inputs, validates that required fields are equal to 8 
     * and performs several transformation cases like Copy = get information from a start character to an end character, 
     * transform = get input information from a specific position, validate and compare it and 
     * default fields = get the information as it arrives, a call is made to the transformation class to EBCDIC.
     * 
     * @author Migration Team
     * @param HasMap<String, Object>
     * @return HasMap<String, Object>
     *
     */
	public HashMap<String, Object> extractionInputFields(HashMap<String, Object> criteriaInput,HashMap<String, Object> requestCriteria) {
		if (requestCriteria.size() == 8) {
			for (String input : criteriaInput.keySet()) {
				String[] inputField = input.split(Constants.DELIMITER);
				String param = inputField[0] + Constants.DELIMITER + inputField[1] + Constants.DELIMITER
						+ inputField[2];
				if (inputField.length > 3) {
					switch (inputField[3]) {
					case Constants.COPY:
						String criteria = (String) requestCriteria.get(param);
						if (criteria.length() >= Integer.parseInt(inputField[5])) {
							data = new String(criteria.substring(Integer.parseInt(inputField[4]), Integer.parseInt(inputField[5])));
							convert = CharacterEncodingConversion.TextToHexEBCDIC(data);
							responseExtraction.put(input, criteriaInput.get(input) + Constants.DELIMITER +  convert);
							msg_isc.append(Constants.HEXA + criteriaInput.get(input) +  convert);
							msg.append(criteriaInput.get(input) + Constants.DELIMITER + convert + Constants.DELIMITER + data + Constants.DELIMITER);
						} else {
							responseExtraction.put(input, Constants.DES_ERROR_LENGTH + criteria);
							responseExtraction.put(Constants.STATUS, Constants.FAILED);
						}
						break;
					case Constants.TRANSFORM:
						String transform = (String) requestCriteria.get(param);
						switch (inputField[1]) {
						case Constants.F126:
							data = new String(transform.contains(Constants.ORIGINAL) ? inputField[4].substring(1)
									: transform.contains(Constants.REFUND) ? inputField[5].substring(1) : Constants.DES_ERROR);
							convert = CharacterEncodingConversion.TextToHexEBCDIC(data);
							responseExtraction.put(input, criteriaInput.get(input) + Constants.DELIMITER +  convert);
							msg_isc.append(Constants.HEXA +  criteriaInput.get(input) + convert);
							msg.append(criteriaInput.get(input) + Constants.DELIMITER + convert + Constants.DELIMITER + data + Constants.DELIMITER);
							break;
						default:
							int value = Integer.parseInt(transform.substring(Integer.parseInt(inputField[4]) - 1, Integer.parseInt(inputField[4])));
							data = new String(String.valueOf(value).equals(inputField[5].substring(0, 1)) ? inputField[5].substring(1) : inputField[6].substring(1));
							convert = CharacterEncodingConversion.TextToHexEBCDIC(data);
							responseExtraction.put(input, criteriaInput.get(input) + Constants.DELIMITER + convert);
							msg_isc.append(Constants.HEXA + criteriaInput.get(input) + convert);
							msg.append(criteriaInput.get(input) + Constants.DELIMITER + convert + Constants.DELIMITER + data + Constants.DELIMITER);
							break;
						}
						break;
					default:
						break;
					}
				} else {
					data = new String((String) requestCriteria.get(param));
					convert = CharacterEncodingConversion.TextToHexEBCDIC(data);
					responseExtraction.put(input, (String) criteriaInput.get(input) + Constants.DELIMITER + convert);
					msg_isc.append(Constants.HEXA + (String) criteriaInput.get(input) + convert);
					msg.append(criteriaInput.get(input) + Constants.DELIMITER + convert + Constants.DELIMITER + data + Constants.DELIMITER);
				}

			}
		} else {
			responseExtraction.put(Constants.ERROR, Constants.INCOMPLETED);
			responseExtraction.put(Constants.STATUS, Constants.FAILED);
		}
		return responseExtraction;
	}

	
	/**
     * Method in charge of the fixed fields where the information found in the key of the configuration json is obtained, 
     * a call is made to the transformation class to EBCDIC.
     * 
     * @author Migration Team
     * @param HasMap<String, Object>
     * @return HasMap<String, Object>
     *
     */
	
	public HashMap<String, Object> extractionFixedField(HashMap<String, Object> fixedCriteria) {
		for (String fixed : fixedCriteria.keySet()) {
			if (fixed.contains(Constants.SPACE)) {
				String spaces = Constants.SPACES;
				for (int i = 0; i < Integer.parseInt(fixed.substring(24, 25)) - 1; i++) {
					spaces = spaces + Constants.SPACES;
				}
				convert = CharacterEncodingConversion.TextToHexEBCDIC(spaces);
				responseExtraction.put(fixed, fixedCriteria.get(fixed) + Constants.DELIMITER + convert);
				msg_isc.append(Constants.HEXA + fixedCriteria.get(fixed) + convert);
				msg.append(fixedCriteria.get(fixed) + Constants.DELIMITER + convert + Constants.DELIMITER + spaces + Constants.DELIMITER);

			} else if (fixed.contains(Constants.UNDERSCORE)) {
				convert = CharacterEncodingConversion.TextToHexEBCDIC(Constants.DELIMITER);
				responseExtraction.put(fixed, fixedCriteria.get(fixed) + Constants.DELIMITER + convert);
				msg_isc.append(Constants.HEXA + fixedCriteria.get(fixed) + convert);
				msg.append(fixedCriteria.get(fixed) + Constants.DELIMITER + convert + Constants.DELIMITER + Constants.DELIMITER + Constants.DELIMITER);
			} else if (fixed.length() > 18) {
				convert = CharacterEncodingConversion.TextToHexEBCDIC(fixed.substring(18));
				responseExtraction.put(fixed, fixedCriteria.get(fixed) + Constants.DELIMITER + convert);
				msg_isc.append(Constants.HEXA + fixedCriteria.get(fixed) + convert);
				msg.append(fixedCriteria.get(fixed) + Constants.DELIMITER + convert + Constants.DELIMITER + fixed.substring(18) + Constants.DELIMITER);
			} else {
				convert = CharacterEncodingConversion.TextToHexEBCDIC(fixed.substring(16));
				responseExtraction.put(fixed, fixedCriteria.get(fixed) + Constants.DELIMITER + convert);
				msg_isc.append(Constants.HEXA + fixedCriteria.get(fixed) + convert);
				msg.append(fixedCriteria.get(fixed) + Constants.DELIMITER + convert + Constants.DELIMITER + fixed.substring(16) + Constants.DELIMITER);
			}

		}				

		return responseExtraction;
	}

	/**
     * Method in charge of the calculated fields where the current date and time are obtained and a 
     * random value is assigned within a range, a call is made to the EBCDIC transformation class.
     * 
     * @author Migration Team
     * @param HasMap<String, Object>
     * @return HasMap<String, Object>
     *
     */
	
	public HashMap<String, Object> extractionCalculateField(HashMap<String, Object> calculateCriteria) {
		try {
			for (String calculate : calculateCriteria.keySet()) {
				if (calculate.contains(Constants.DATE)) {
					SimpleDateFormat dayformat = new SimpleDateFormat(calculate.substring(25));
					String date = dayformat.format(Calendar.getInstance().getTime());
					convert = CharacterEncodingConversion.TextToHexEBCDIC(date);
					responseExtraction.put(calculate, calculateCriteria.get(calculate) + Constants.DELIMITER + convert);
					msg_isc.append(Constants.HEXA + calculateCriteria.get(calculate) + convert);
					msg.append(calculateCriteria.get(calculate) + Constants.DELIMITER + convert + Constants.DELIMITER + date + Constants.DELIMITER);
				} else if (calculate.contains(Constants.HOUR)) {
					DateFormat hourformat = new SimpleDateFormat(calculate.substring(25));
					String hour = hourformat.format(Calendar.getInstance().getTime());
					convert = CharacterEncodingConversion.TextToHexEBCDIC(hour);
					responseExtraction.put(calculate, calculateCriteria.get(calculate) + Constants.DELIMITER + convert);
					msg_isc.append(Constants.HEXA + calculateCriteria.get(calculate) + convert);
					msg.append(calculateCriteria.get(calculate) + Constants.DELIMITER + convert + Constants.DELIMITER + hour + Constants.DELIMITER);
				} else if (calculate.contains(Constants.RANDOM)) {
					Random random = new Random();
					int numberRandom = (int) (random.nextInt(999999) + 1);
					convert = CharacterEncodingConversion.TextToHexEBCDIC(String.valueOf(numberRandom));
					responseExtraction.put(calculate,calculateCriteria.get(calculate) + Constants.DELIMITER + convert);
					msg_isc.append(Constants.HEXA + calculateCriteria.get(calculate) + convert);
					msg.append(calculateCriteria.get(calculate) + Constants.DELIMITER + convert + Constants.DELIMITER + String.valueOf(numberRandom) + Constants.DELIMITER);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseExtraction;

	}

}
