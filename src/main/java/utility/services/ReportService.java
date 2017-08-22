package utility.services;

import org.testng.Assert;
import utility.Log;

public class ReportService {

	public static void assertTrue(Boolean condition, String errorMessage ) {
		
		if (!condition){
			Log.info("");
			Log.error("ACTUAL RESULT:");
			Log.error(errorMessage);
		}

		Assert.assertTrue(condition);
		
	}
	
	public static void assertFalse(Boolean condition, String errorMessage ) {
	
		if (condition){
			Log.info("");
			Log.error("ACTUAL RESULT:");
			Log.error(errorMessage);
		} 
		
		Assert.assertFalse(condition);
		
	}

	public static <T> void assertEquals(T condition1, T condition2, String errorMessage) {

        String error = "ACTUAL RESULT:\n"+errorMessage+"\nExpected: \"" + condition2 + "\", but found: \"" + condition1 + "\".";

        if (condition1 instanceof String){
            if (!((String) condition1).equalsIgnoreCase((String)condition2)) {
                Log.error(error);
            }
        }
        else {
            if (!(condition1.equals(condition2))) {
                Log.error(error);
            }
        }
		
		Assert.assertEquals(condition1, condition2);
		
	}

    public static void assertEquals(double expected, double actual, double delta, String errorMessage) {

        String error = "ACTUAL RESULT:\n" + errorMessage + "\nExpected: \"" + expected + "\", but found: \"" + actual + "\".";

        if (Double.isInfinite(expected)) {
            if (expected != actual) {
                Log.error(error);
            }
        } else if (Math.abs(expected - actual) > delta) {
            Log.error(error);
        }

        Assert.assertEquals(expected, actual, delta);
    }

    public static <T> void assertNotEquals(T condition1, T condition2, String errorMessage) {

        if (condition1.equals(condition2)){
            Log.info("");
            Log.error("ACTUAL RESULT:");
            Log.error(errorMessage);
            Log.error("Expected: \"not " + condition2 + "\", but found: \"" + condition1 + "\".");
        }

        Assert.assertNotEquals(condition1, condition2);

    }

    public static void catchException(Exception e){
        Assert.assertTrue(false, String.valueOf(e));
    }

}
