package utility.services;

import lombok.extern.log4j.Log4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.Constants;
import utility.Log;

import java.util.Collections;

import static utility.Constants.ELEMENT_TIMEOUT;
import static utility.Log.info;
import static utility.Log.warn;
import static utility.services.ReportService.assertTrue;
import static utility.services.ReportService.catchException;
import static utility.services.WebElementService.*;

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

    public static void waitForCookieUnset(String cookieName, WebDriver driver){

        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_TIMEOUT);
        wait.until((WebDriver webDriver) -> driver.manage().getCookieNamed(cookieName)==null);
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

    public static void waitForTextVisible(String text, WebElement element, WebDriver driver) {

        try {
            WebDriverWait wait = new WebDriverWait(driver,40);
            wait.until(ExpectedConditions.textToBePresentInElement(element, text));
            info("TEXT: \"" + text +"\" is present.");
        }
        catch (TimeoutException e){
            assertTrue(false, "TEXT: \"" + text + "\" is not presents.");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "This element not found.");
        }

    }

    public  static void waitForTextIsEmpty(WebElement element){

        try {
            int attempt_counter = 0;
            while (!(element.getText().isEmpty())){
                sleep(1);
                attempt_counter++;
                if (attempt_counter == 10){
                    assertTrue(false,"Unnecessary string still present after "+attempt_counter+" seconds waiting "+element.getText());
                    break;
                }
            }
        }
        catch ( NoSuchElementException e){
            Log.error("Caught exception" + e);
        }
    }

    public  static void pageReadyStateLoaderWait(WebDriver driver){
        pageReadyStateLoaderWait(driver, Constants.PAGE_TIMEOUT);
    }

    public static void pageReadyStateLoaderWait(WebDriver driver, int timeout) {

        try {
            new WebDriverWait(driver, timeout).until((WebDriver webDriver) ->
                    ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        } catch (WebDriverException e) {
            warn(e);
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

    public  static void waitForCookieValue(String cookie, String value, WebDriver driver){

        int attempt = 0;
        boolean flag = true;
        info("Waiting for cookie \""+cookie+"\" became - "+value+".");
        while (flag && attempt<10){
            attempt++;
            sleep(1);
            if (driver.manage().getCookieNamed(cookie).getValue().equals(value)){
                flag = false;
                info("Cookie "+cookie+"\" became - "+value+".");
            }
        }
    }

    public static void waitForValue(WebElement element, String text){

        boolean flag = true;
        int attempt = 0;
        while (flag && attempt<10){
            attempt++;
            sleep(1);
            if (element.getAttribute("value").equals(text)){
                flag = false;
                info("Element has expected value.");
            }
        }
    }

    public static void waitPageLoader(String url, WebDriver driver) {
        waitPageLoader(url, Constants.PAGE_TIMEOUT, driver);
        pageReadyStateLoaderWait(driver);
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

    public static void waitForAttributeValue(final WebElement element, final String attribute, final String text, WebDriver driver){

        try {
            WebDriverWait wait = new WebDriverWait(driver,Constants.PAGE_TIMEOUT);

            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    String enabled = element.getAttribute(attribute);
                    return enabled.contains(text);
                }
            });
        }
        catch (TimeoutException e){
            assertTrue(false, "Value: \""+text+"\". Attribute: \"" + attribute + "\" is not presents in element.");
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

    public static void waitForElementIsSelected(WebElement el, WebDriver driver) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, ELEMENT_TIMEOUT);
            wait.until((WebDriver webDriver) -> el.isSelected());
        }catch (TimeoutException e){
            catchException(e);
        }
    }

    public static void waitForChangeUrl(WebDriver driver, final String urlPart) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    return (!driver.getCurrentUrl().contains(urlPart.toLowerCase()));
                }
            });
        } catch (TimeoutException e) {
            catchException(e);

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

    public static void waitForAttributeAppear(WebDriver driver, WebElement element, String attrName){

        try {
            WebDriverWait wait = new WebDriverWait(driver, ELEMENT_TIMEOUT);
            wait.until((WebDriver webDriver) ->  element.getAttribute(attrName)!=null);
        }
        catch (TimeoutException e){
            catchException(e);
        }
    }

    public static void waitForAttributeValueNotEmpty(WebDriver driver, WebElement element, String attrName) {

        WebDriverWait wait = new WebDriverWait(driver, ELEMENT_TIMEOUT);
        wait.withMessage("Failed to wait for attribute " + attrName + " is not empty.");
        wait.until((WebDriver webDriver) -> !element.getAttribute("href").isEmpty());

    }

    public static void waitJqueryComplete(WebDriver driver,long timeout){

        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until((WebDriver webDriver) -> ((JavascriptExecutor)driver).executeScript("return jQuery.active").toString().equals("0"));
        } catch (TimeoutException e) {
            info("No one jQuery activity or activity continues");
        }
    }

    /** The method waiting when text is change on element.
     * @param element WebElement in which text should be change.
     * @param driver WebDriver
     */
    public static void waitForElementTextIsChange(WebElement element, String newText, WebDriver driver){

        try {
            (new WebDriverWait(driver, ELEMENT_TIMEOUT))
                    .until((WebDriver webDriver) -> getElementText(element, "element").equals(newText));
        } catch (TimeoutException e) {
            catchException(e);
        }
    }

    /** The method waiting when text is appear on element.
     * @param element WebElement in which text should be present.
     * @param driver WebDriver
     */
    public static void waitForElementTextIsPresent(WebElement element, WebDriver driver){

        try {
            (new WebDriverWait(driver, ELEMENT_TIMEOUT))
                    .until((WebDriver webDriver) -> !getElementText(element, "element").isEmpty());
        } catch (TimeoutException e) {
            catchException(e);
        }
    }

    public static void waitForElementNotVisible(WebElement element, int delay, WebDriver driver) {

        if (elementIsDisplayed(element, "")) {
            info("Wait for element will not be displayed in " + delay + " seconds.");
            try {
                WebDriverWait wait = new WebDriverWait(driver, delay);
                wait.until(ExpectedConditions.invisibilityOfAllElements(Collections.singletonList(element)));
                info("Element is not appear.");
            } catch (TimeoutException e) {
                warn("ELEMENT: \"" + element + "\" is present.");
                catchException(e);
            }
        }
    }

    public static void waitForTextVisible(String text, WebElement element, int delay, WebDriver driver) {

        try {
            WebDriverWait wait = new WebDriverWait(driver,delay);
            wait.until(ExpectedConditions.textToBePresentInElement(element, text));
            info("TEXT: \"" + text +"\" is present.");
        }
        catch (TimeoutException e){
            assertTrue(false, "TEXT: \"" + text + "\" is not presents.");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "This element not found.");
        }

    }

    public static void waitForElementChangePosition(WebElement element, int x, int y, WebDriver driver) {
        info("Wait for element will change position in " + ELEMENT_TIMEOUT + " seconds.");
        try {
            WebDriverWait wait = new WebDriverWait(driver, ELEMENT_TIMEOUT);
            wait.until((WebDriver webDriver) ->
                    element.getLocation().getX() != x && element.getLocation().getY() != y);
            info("Element changed position");
        }
        catch (TimeoutException e){
            assertTrue(false, "Element didn't change position");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "This element not found.");
        }
    }
}
