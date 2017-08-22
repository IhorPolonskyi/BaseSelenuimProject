package utility.services;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static utility.Log.info;
import static utility.services.ReportService.assertTrue;
import static utility.services.WebElementService.moveToCoordinate;

public class ManageUrlService {

    public static void getURL(String url, WebDriver driver) {
        driver.getCurrentUrl();
        info("Navigate to \""+url+"\".");
        try {
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.get(url);

            info("Navigate to \""+url+"\" finished.");
            moveToCoordinate(0,0,driver);
        }
        catch (TimeoutException e){
            stopLoad(driver);
        }
    }

    public  static void refreshPage(WebDriver driver){
        try {
            driver.navigate().refresh();
            info("Page was refreshed.");
        }
        catch (WebDriverException e){
            stopLoad(driver);
        }
    }

    public  static String getCurrentURL(WebDriver driver){
        info("Current URL:"+driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }


    public static void switchToLastWindow(WebDriver driver){
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    return (driver.getWindowHandles().size() > 1) ;
                }
            });
            for (String win:driver.getWindowHandles()){
                driver.switchTo().window(win);
            }
            info("Switch to another window.");
        }
        catch (TimeoutException e){
            assertTrue(false, "You have only one window.");
        }
        driver.manage().window().maximize();
        info("Maximize window.");
    }

    public static void switchToLastWindowClose(WebDriver driver){
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    return (driver.getWindowHandles().size() > 1) ;
                }
            });
            String last = "";
            Iterator<String> iterator = driver.getWindowHandles().iterator();
            while (iterator.hasNext()){
               last=iterator.next();
            }
            for (String win:driver.getWindowHandles()){
                if (win.equals(last)) {
                    driver.switchTo().window(win);
                }
                else driver.close();
            }
            info("Switch to another window.");
        }
        catch (TimeoutException e){
            assertTrue(false, "You have only one window.");
        }
        driver.manage().window().maximize();
        info("Maximize window.");
    }


    public static void scrollDown(WebDriver driver){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
    }

    public static void stopLoad(WebDriver driver){
        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        info("Timeout on loading page \""+driver.getCurrentUrl()+"\".");
    }


    public static void scrollDown(WebDriver driver, int px){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("window.scrollBy(0,"+px+")", "");
    }

}
