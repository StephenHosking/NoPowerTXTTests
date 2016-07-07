package au.com.cba.nopowersms;

import junit.framework.TestCase;

public class Assert extends TestCase {
	
	static void areEqual(int actual, int expected, String msg) {
		if (actual != expected){
			fail(String.format("%s. actual = %d, expected = %d",msg,actual,expected));
		}				
	}

	static void isNotNull(Object obj, String msg) {
		if (obj == null){
			fail(String.format("%s. Expected not `null`, but got `null`.",msg));
		}				
	}

	static void isTrue(boolean actual, String msg) {
		if (!actual){
			fail(String.format("%s. Expected true, got false.",msg));
		}				
	}

	static void isFalse(boolean actual, String msg) {
		if (actual){
			fail(String.format("%s. Expected false, got true.",msg));
		}				
	}

	public static void areEqual(String actual, String expected, String msg) {
		if (!actual.equals(expected)){
			fail(String.format("%s. actual = %s, expected = %s",msg,actual,expected));
		}						
	}

	public static void areEqual(boolean actual, boolean expected, String msg) {
		if (!actual == expected){
			fail(String.format("%s. actual = %b, expected = %b",msg,actual,expected));
		}						
		
	}

	public static void isNull(Object obj, String msg) {
		if (obj != null){
			fail(String.format("%s. Expected not `null`, but got `null`.",msg));
		}				
		
	}

	public static void areNotEqual(String resellerMessage, String actual,
			String expected) {
		if (actual.equals(expected)){
			fail(String.format("%s. actual = %s, expected = %s",resellerMessage,actual,expected));
		}						
		
	}


}
