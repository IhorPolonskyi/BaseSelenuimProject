package utility.services;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static utility.Log.info;
import static utility.Log.warn;
import static utility.services.ManageUrlService.refreshPage;
import static utility.services.ReportService.assertTrue;

public class CookiesService {

    private static WebDriver driver;
    public CookiesService(WebDriver driver){
        this.driver = driver;
    }

    public static String getCookieValue(String cookieName){
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

    public static void deleteCookie(String cookieName){
        if (driver.manage().getCookieNamed(cookieName)!=null){
            driver.manage().deleteCookieNamed(cookieName);
            info("Delete \""+cookieName+"\" cookie.");
        }
        else {
            warn("Couldn't find \""+cookieName+"\" cookie.");
        }

    }

    public static boolean verifyCookieIsSet(String cookieName){
        return driver.manage().getCookieNamed(cookieName) != null;
    }

    public static void clearCookies(){
        driver.manage().deleteAllCookies();
        info("Delete all cookies.");
        refreshPage();
    }

    public static void setCookie(String name, String value) {
        Cookie cookie = driver.manage().getCookieNamed(name);

        driver.manage().deleteCookie(cookie);
        driver.manage().addCookie(
                new Cookie.Builder(cookie.getName(), value)
                        .domain(cookie.getDomain().replace(".www", ""))
                        .expiresOn(cookie.getExpiry())
                        .path(cookie.getPath())
                        .isSecure(cookie.isSecure())
                        .build()
        );

        info("Set cookie " + name + ":" + value);
    }

    public static String getCookieExpireDate(String cookieName){
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        if (driver.manage().getCookieNamed(cookieName)!=null){
            Date  date = driver.manage().getCookieNamed(cookieName).getExpiry();
            if (date!=null){
                info("Cookie: \"" + cookieName + "\" has Expire Date - \"" + dateFormat.format(date) + "\".");
                return dateFormat.format(date);
            }
            else return null;
        }
        else {
            assertTrue(false, "Cookie: \"" + cookieName + "\" was not found after timeout.");
            return null;
        }

    }

    public static void addCookie(String name, String value){
        Cookie.Builder builder = new Cookie.Builder(name, value);
        Cookie cookie = builder.build();
        driver.manage().addCookie(cookie);
        info("Add cookie "+name+" with value "+value);
    }

    /**
     * @return List names of cookie available on current page
     */
    public static List<String> getNamesCookies(){
        return driver.manage().getCookies().stream()
                .map(Cookie::getName)
                .collect(Collectors.toList());
    }

}