package utility.services;

import utility.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by igorp on 11/05/17.
 */
public class PropertyReader {

    public Properties properties;

    public PropertyReader(String file){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);
        try {
            properties = new Properties();
            if (file.contains(".properties")){
                properties.load(inputStream);
            }
            else {
                properties.load(new InputStreamReader(inputStream, "UTF-8"));
            }
        }
        catch (IOException e){
            Log.error("IOException " + e.getMessage());
        }
    }

    public String getPropVal(String propName){
        return properties.getProperty(propName);
    }

    public String getValue(String key){
        return properties.getProperty(key)==null ? null :convertUTF(properties.getProperty(key));
    }

    public String getAnyVal(String key){
        return System.getProperty(key)!=null ? System.getProperty(key)
                : getValue(key);
    }

    public Properties getProperties() {
        return this.properties;
    }

    public  static String convertUTF(String origin){
        String convert = null;
        try {
            convert = new String(origin.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException e){
            Log.error(e.toString());
        }

        return convert;
    }
}
