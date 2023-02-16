package co.com.msacceptancecriteriaisc.support;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * Class in charge of converting characters to hexadecimal ASCII or EBCDIC or from ASCII to text 
 * or from EBCDIC to text and from EBCDIC to ASCII.
 * 
 * @author Migration Team
 */
public class CharacterEncodingConversion {

	
	/**
	 * Conversion method where an EBCDIC string is received and transformed to ASCII.
	 * 
	 * @author Migration Team
	 * @param String
	 * @return String
	 *
	 */
	public static String EBCDICToASCII(String text) {
		String hexEBCDIC = text;
		byte[] ebcdicBytes = hexStringToByteArray(hexEBCDIC);
		String asciiString = new String(ebcdicBytes, Charset.forName("Cp037"));
		String hexASCII = byteArrayToHexString(asciiString.getBytes(StandardCharsets.US_ASCII));
		return hexASCII;
	}

	
	/**
	 * Conversion method where a text type string is received and transformed into ASCII.
	 * 
	 * @author Migration Team
	 * @param String
	 * @return String
	 *
	 */
	
	public static String TextToASCII(String text) {
		byte[] asciiBytes = text.getBytes(StandardCharsets.US_ASCII);
		String hexASCII = byteArrayToHexString(asciiBytes);
		return hexASCII;
	}

	
	/**
	 * Conversion method where a text type ASCII is received and transformed into EBCDIC.
	 * 
	 * @author Migration Team
	 * @param String
	 * @return String
	 *
	 */
	
	public static String ASCIIToEBCDIC(String text) {
		String hexASCII = text;
		byte[] asciiBytes = hexStringToByteArray(hexASCII);
		String asciiString = new String(asciiBytes, StandardCharsets.US_ASCII);
		byte[] ebcdicBytes = asciiString.getBytes(Charset.forName("Cp037"));
		String hexEBCDIC = byteArrayToHexString(ebcdicBytes);
		return hexEBCDIC;
	}

	
	/**
	 * Conversion method where a text type ASCII is received and transformed into TEXT.
	 * 
	 * @author Migration Team
	 * @param String
	 * @return String
	 *
	 */
	
	public static String ASCIIToText(String text) {
		String hexASCII = text;
		byte[] asciiBytes = hexStringToByteArray(hexASCII);
		String asciiString = new String(asciiBytes, StandardCharsets.US_ASCII);
		return asciiString;
	}
	
	/**
	 * Conversion method where a text type string is received and transformed into EBCDIC.
	 * 
	 * @author Migration Team
	 * @param String
	 * @return String
	 *
	 */
	
	public static String TextToHexEBCDIC(String text) {
        byte[] ebcdicBytes = text.getBytes(Charset.forName("Cp037"));
        String hexEBCDIC = byteArrayToHexString(ebcdicBytes);
		return hexEBCDIC;
	}

	/**
	 * Method where it receives a string of type string and returns an array of type byte.
	 * 
	 * @author Migration Team
	 * @param String
	 * @return byte[]
	 *
	 */
	
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Method where it receives an array of type byte and returns a string of type string.
	 * 
	 * @author Migration Team
	 * @param byte[]
	 * @return String
	 *
	 */
	
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

}
