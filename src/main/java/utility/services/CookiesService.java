package utility.services;

import org.openqa.selenium.WebDriver;

import static utility.Log.info;
import static utility.services.ReportService.assertTrue;

public class CookiesService {

    public static String getCookieValue(String cookieName, WebDriver driver){
        if (driver.manage().getCookieNamed(cookieName)!=null){
            String value = driver.manage().getCookieNamed(cookieName).getValue();
            info("Cookie: \"" + cookieName + "\" has value - \"" + value + "\".");
            return value;
        }
        else {
            assertTrue(false, "Cookie: \"" + cookieName + "\" was not found after timeout.");
            return null;
        }
    }
}