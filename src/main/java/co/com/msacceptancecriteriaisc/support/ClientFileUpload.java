package co.com.msacceptancecriteriaisc.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import co.com.msacceptancecriteriaisc.constants.Constants;

/**
 * Class in charge of calling the file upload service through an http request by a rest type call.
 * 
 * @author Migration Team
 */
public class ClientFileUpload {

	/**
	 * Method to control the consumption of the file upload service through a rest request with a json type 
	 * response where a conversion is made to Map<String, Object>.
	 * 
	 * @author Migration Team
	 * @param String
	 * @return ConcurrentHasMap<String, String>
	 * @throws Exception
	 *
	 */
	public ConcurrentHashMap<String, Object> getDataFile(String file) {
		Map<String, Object> dataFile = null;
		ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
		try {
			StringBuilder resultado = new StringBuilder();
			URL url = new URL(Constants.ENDPOINTUPLOADFILE + file);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestMethod("GET");
			conexion.setConnectTimeout(2000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = reader.readLine()) != null) {
				resultado.append(linea);
			}
			reader.close();
			dataFile = (Map<String, Object>) (new JSONObject(resultado.toString()).toMap());
			map.putAll(dataFile);
		} catch (Exception e) {
			map.put(Constants.STATUS, Constants.ERROR);
			map.put(Constants.ERROR, e.getMessage());

		}
		return map;

	}

}
