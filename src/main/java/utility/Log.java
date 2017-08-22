package utility;

import org.apache.log4j.Logger;

/**
 * Created by igorp on 11/05/17.
 */
public class Log {

    private static Logger Log = Logger.getLogger(utility.Log.class.getName());

    public static <T extends Object> void info(T message) {
        Log.info(String.valueOf(message));
    }
    public static <T extends Object> void warn(T message) {
        Log.warn(String.valueOf(message));
    }
    public static <T extends Object> void error(T message) {
        Log.error(String.valueOf(message));
    }

}


