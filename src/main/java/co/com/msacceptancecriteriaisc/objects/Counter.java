package co.com.msacceptancecriteriaisc.objects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class where the variables for counting successful, failed and total transactions are defined.
 * 
 * @author Migration Team
 */
public class Counter {

	public static AtomicInteger totalSuccess = new AtomicInteger();
	public static AtomicInteger totalFailed = new AtomicInteger();
	public static AtomicInteger totalProcess = new AtomicInteger();
}
