package co.com.msacceptancecriteriaisc.objects;

import org.json.JSONObject;

import co.com.msacceptancecriteriaisc.constants.Constants;


/**
 * Class that defines the method of tracking the consumed operations where the transaction status, 
 * the execution time, is obtained.
 * 
 * @author Migration Team
 */
public class TrackingAcceptanceCriterial {
	
	public static JSONObject processTransaction = new JSONObject();

	/**
	 * Control method to monitor one by one the consumed operations, where it returns the 
	 * version, package, service, class, endpoint, execution time and 
	 * makes a call to determine the number of successful, failed and total transactions.
	 * 
	 * @author Migration Team
	 * @param JSONObject
	 * @return JSONObject
	 *
	 */
	public JSONObject getInfoAcceptance(JSONObject infoacceptancriterial) {
		processTransaction = new JSONObject();
		Long initialTime = System.nanoTime();
		processTransaction = infoacceptancriterial;
		processTransaction = processTransaction(infoacceptancriterial);
		processTransaction.put("VERSION", Constants.VERSION);
		processTransaction.put("SERVICE", Constants.SERVICE);
		processTransaction.put("PACKAGE", Constants.PACKAGE);
		processTransaction.put("CLASS", Constants.CLASS);
		processTransaction.put("ENDPOINT", Constants.ENDPOINT);
		processTransaction.put("TIME", (System.nanoTime() - initialTime) + " Nanoseconds");
		return processTransaction;
	}

	/**
	 * Control method that performs a one-to-one count of successful transactions, 
	 * failed transactions and a total sum of the transactions. 
	 * 
	 * @author Migration Team
	 * @param JSONObject
	 * @return JSONObject
	 *
	 */
	
	public JSONObject processTransaction(JSONObject infoacceptancriterial) {
		processTransaction.put(Constants.SUCCTRAN, infoacceptancriterial.get(Constants.STATUS).equals(Constants.SUCCESS) 
				? Counter.totalSuccess.incrementAndGet() : Counter.totalSuccess.get());
		processTransaction.put(Constants.FAILTRAN, infoacceptancriterial.get(Constants.STATUS).equals(Constants.FAILED) 
				? Counter.totalFailed.incrementAndGet() : Counter.totalFailed.get());
		processTransaction.put(Constants.TOTALTRAN, Counter.totalProcess.incrementAndGet());
		return processTransaction;

	}
}
