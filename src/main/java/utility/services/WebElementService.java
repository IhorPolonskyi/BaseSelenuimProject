package utility.services;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static utility.Log.*;
import static utility.services.ManageUrlService.scrollDown;
import static utility.services.ManageUrlService.stopLoad;
import static utility.services.ReportService.assertTrue;

public abstract class WebElementService {

    public static boolean elementIsDisplayed(WebElement element, String elementName){

        try {
            if (element != null && element.isDisplayed()){
                //Log.info("\"" + elementName + "\" is displayed.");
                return true;
            }
            else {
                info("\"" + elementName + "\" is not displayed.");
                return false;
            }
        }
        catch (NoSuchElementException e){
            info("\"" + elementName + "\" is NOT displayed.");
            return false;
        }
        catch (ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not visible.");
            return false;
        }
        catch (StaleElementReferenceException e){
            //ReportService.assertTrue(false, "\"" + elementName + "\" was not in the cache.");
            return false;
        }
    }

    public static void clickOnElement(WebElement element, String elementName, WebDriver driver){

        try {
            WebDriverWait wait = new WebDriverWait(driver,20);
            wait.until(ExpectedConditions.elementToBeClickable(element));}
        catch (TimeoutException ex){
            info("\"" + elementName + "\" is not clickable.");
            clickHack(element, elementName, driver);
            info("Click on \"" + elementName + "\".");
            return;
        }
        try {
            element.click();
            info("Click on \"" +elementName+"\".");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\"" + elementName + "\" was not found on page after timeout.");
        }
        catch (ElementNotVisibleException e){
            error("ElementNotVisibleException");
            clickHack(element, elementName, driver);
        }
        catch (TimeoutException e){
            stopLoad(driver);
        }
        catch (StaleElementReferenceException e){
            warn("StaleElementReferenceException.");
            info("Click on \"" +elementName+"\".");
            element.click();
        }
        catch (WebDriverException e){
            error("WebDriverException" +e);
            clickHack(element, elementName, driver);
        }
    }

    private static void clickHack(WebElement element, String elementName, WebDriver driver){
        boolean flag = true;
        int attempt = 0;

        while (flag && attempt<5){
            attempt++;
            try {
                info("\"" + elementName + "\" is hide by another element, move down.");
                scrollDown(driver,500);
                moveToCoordinate(0, 0, driver);
                element.click();
                info("Click on \"" +elementName+"\".");
                flag = false;
            }
            catch (WebDriverException ignored){}
        }
    }

    public static String getElementValue(WebElement element, String elementName){
        return getElementAttribute(element, elementName, "value");
    }

    public static void sendKeys(WebElement element, String elementName, String inputText){

        try {
            element.sendKeys(inputText);
            info("\"" + elementName + "\" input text: \"" + inputText + "\".");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\"" + elementName + "\" was not found on page after timeout.");
        }
        catch (ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not visible.");
        }
    }

    public static void sendKeysClear(WebElement element, String elementName, String inputText, WebDriver driver){

        try {
            WaiterService.waitForElementVisible(element, driver);
            int attempt = 0;
            element.clear();
            element.sendKeys(inputText);
            while (element.getAttribute("value").length()!=inputText.length() && attempt<5){
                attempt++;
                element.clear();
                element.sendKeys(inputText);
            }
            info("\"" + elementName + "\" input text: \"" + inputText + "\".");
        }
        catch (NoSuchElementException e ){
            assertTrue(false, "\"" + elementName + "\" was not found on page after timeout.");
        }
        catch (ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not visible.");
        }
        catch (InvalidElementStateException e){
            warn("Catch InvalidElementStateException.");
            WebDriverWait wait = new WebDriverWait(driver,10);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.clear();
            element.sendKeys(inputText);
        }
    }

    public static void moveToCoordinate(int x, int y, WebDriver driver) {

        Actions actions = new Actions(driver);
        actions.moveByOffset(x, y).build().perform();
        info("Move to coordinate "+x+"x"+y);
    }

    public static boolean attributeIsPresent(WebElement element, String attribute){
        boolean flag;
        try {
            flag = element.getAttribute(attribute) != null && !element.getAttribute(attribute).isEmpty();
        }
        catch (NoSuchElementException e){
            flag = false;
            assertTrue(flag,"Element not found.");
        }
        return flag;
    }

    public static String getElementAttribute(WebElement element, String elementName, String attribute){
        String attributeValue = "";
        try {
            //Get value.
            if (element.getAttribute(attribute) != null){
                attributeValue = element.getAttribute(attribute);
            }
            else {
                assertTrue(false,"Attribute \""+attribute+"\" not present.");
            }
            info(attribute + " \"" + elementName +"\" = \"" + attributeValue + "\".");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\"" + elementName + "\" was not found on page after timeout.");
        }
        catch (ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not visible.");
        }
        return attributeValue;
    }

}
