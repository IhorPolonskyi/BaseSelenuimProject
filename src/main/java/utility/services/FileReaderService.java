package utility.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;

/**
 * Created by igorp on 11/05/17.
 */
public class FileReaderService {

    public static List<String> listReader(String fileLocation){
        BufferedReader in;
        List<String> myList = new ArrayList<>();
        try {
            in = new BufferedReader(new FileReader(fileLocation));
            String str;
            while ((str = in.readLine()) != null) {
                myList.add(str);
            }
            in.close();
        }
        catch (IOException e){
            assertTrue(false, "Catch an exception " + e);
        }

        return myList;
    }

    public static Map<String, String> getMap(String fileLocation) {
        PropertyReader propertyReader = new PropertyReader(fileLocation);
        return new HashMap(propertyReader.getProperties());
    }

    public static Object[][] dataProviderFile(String filePath){
        List<String> emails = listReader("src/test/resources/properties/" + filePath);
        Object[][] objects = new Object[emails.size()][1];
        for (int i = 0; i < emails.size(); i++) {
            objects[i][0] = emails.get(i);
        }
        return objects;
    }

    public static String fileReader(File file){
        BufferedReader in;
        String result = "";
        try {
            in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                result+=str+"\n";
            }
            in.close();
        }
        catch (IOException e){
            assertTrue(false, "Catch an exception " + e);
        }

        return result;
    }
}
