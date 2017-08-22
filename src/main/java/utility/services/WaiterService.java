package utility.services;

import lombok.extern.log4j.Log4j;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.IndexPage;
import utility.Constants;

import static org.openqa.selenium.support.PageFactory.initElements;
import static utility.Constants.ELEMENT_TIMEOUT;
import static utility.Log.info;
import static utility.Log.warn;
import static utility.services.ReportService.assertTrue;
import static utility.services.WebElementService.elementIsDisplayed;

@Log4j
public class WaiterService {

    public static void waitForCookie(String cookieName, WebDriver driver){

        int attempt_counter = 0;
        while (driver.manage().getCookieNamed(cookieName)==null ||
                (driver.manage().getCookieNamed(cookieName) != null && driver.manage().getCookieNamed(cookieName).getValue() == null))
        {
            WaiterService.sleep(1);
            attempt_counter++;
            if (attempt_counter == ELEMENT_TIMEOUT){
                warn("Cookie: \"" + cookieName + "\" was NOT found, break by attempt counter.");
                break;}
        }
    }

    @Deprecated
    public static void waitForElementDisappear(WebElement element, String elementName){

        info("Wait for element: \"" + elementName + "\" disappear.");
        int attempt_counter = 0;
        while (elementIsDisplayed(element,elementName)) {
            sleep(1);
            attempt_counter++;
            if (attempt_counter == 5){
                assertTrue(false, "\"" + elementName + "\" not disappeared after timeout.");
                break;
            }

        }
    }

    public static void waitForElementVisible(WebElement element, WebDriver driver) {

        try {
            WebDriverWait wait = new WebDriverWait(driver,20);
            wait.until(ExpectedConditions.visibilityOf(element));
        }
        catch (TimeoutException e){
            assertTrue(false, "ELEMENT: \"" + element + "\" is not presents.");
        }
        catch (StaleElementReferenceException e){
            warn("ELEMENT: \"" + element + "\" is not found in the cache.");
        }

    }

    public static void waitForElementVisible(WebElement element, int delay, WebDriver driver) {

        try {
            WebDriverWait wait = new WebDriverWait(driver,delay);
            wait.until(ExpectedConditions.visibilityOf(element));
        }
        catch (TimeoutException e){
            warn("ELEMENT: \"" + element + "\" is not presents.");
        }

    }

    public static void sleep(int seconds){

        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitPageLoader(String url, WebDriver driver) {
        waitPageLoader(url, Constants.PAGE_TIMEOUT, driver);
    }

    public static void waitPageLoader(String url, int seconds, WebDriver driver) {

        try {
            info("Waiting for \"" + url + "\" page.");
            int attempt = 0;
            while (!driver.getCurrentUrl().contains(url) && attempt < seconds) {
                attempt++;
                sleep(1);
            }
            info("Waiting for \"" + url + "\" page during " + attempt + " seconds.");
            if (!driver.getCurrentUrl().contains(url)) {
                assertTrue(false, "Expected page hasn't loaded  by timeout.\n" +
                        "                                   Current url:" + driver.getCurrentUrl());
            }
        } catch (TimeoutException e) {
            ManageUrlService.stopLoad(driver);
        }
    }

    public static void waitForElementDisappear(WebElement element, int seconds, String elementName){

        info("Wait for element: \"" + elementName + "\" disappear.");
        int attempt_counter = 0;
        while (elementIsDisplayed(element,elementName)) {
            sleep(1);
            attempt_counter++;
            if (attempt_counter == seconds){
                assertTrue(false, "\"" + elementName + "\" not disappeared after timeout.");
                break;
            }

        }
    }

    public static void waitPageLoader(WebDriver driver){

        try {
            info("Waiting for change url.");
            final String previousUrl = driver.getCurrentUrl();
            WebDriverWait wait = new WebDriverWait(driver, Constants.PAGE_TIMEOUT);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return (!driver.getCurrentUrl().equals(previousUrl));
                }
            });
        }
        catch (TimeoutException e){
            assertTrue(false, "Expected page hasn't changed  by timeout.\n" +
                    "                                   Current url:"+driver.getCurrentUrl());
        }
    }

    public static void waitForElementClickable(WebElement element, WebDriver driver) {

        try {
            WebDriverWait wait = new WebDriverWait(driver,20);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }
        catch (TimeoutException e){
            assertTrue(false, "ELEMENT: \"" + element + "\" is not clickable.");
        }
        catch (StaleElementReferenceException e){
            warn("ELEMENT: \"" + element + "\" is not found in the cache.");
        }
    }


    public static void waitCssLoad(WebDriver driver){
        IndexPage indexPage = initElements(driver, IndexPage.class);
        waitForElementClickable(indexPage.favorites, driver);
    }
}
