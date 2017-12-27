package utility.services;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.stream.Stream;

import static utility.Log.*;
import static utility.services.ManageUrlService.scrollDown;
import static utility.services.ManageUrlService.stopLoad;
import static utility.services.PressKeysService.pressBackSpace;
import static utility.services.ReportService.assertTrue;
import static utility.services.WaiterService.sleep;

public class WebElementService {

    private static WebDriver driver;
    public WebElementService(WebDriver driver){
        this.driver = driver;
    }

    public static boolean checkFocusOnElement(WebElement element, String elementName){
        int attempt_counter = 0;
        while (!element.equals(driver.switchTo().activeElement())){
            sleep(1);
            attempt_counter++;

            if (attempt_counter == 5){
                error("Break, element isn't focused by timeout.");
                break;
            }
        }
        //Check that field is focused.
        if(element.equals(driver.switchTo().activeElement())){
            info("\"" + elementName + "\" field is focused.");
            return true;
        }
        else {
            info("\"" + elementName + "\" field is NOT focused.");
            return false;
        }
    }

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

    public static boolean elementIsPresent(WebElement element) {

        try {
            element.getText();
            //Log.info(element + " is present.");
            return true;
        }
        catch (NoSuchElementException | NullPointerException e){
            //Log.info(element+ " is not present.");
            return false;
        }
    }

    public static boolean elementIsEnable(WebElement element, String elementName){

        if (element.isEnabled()){
            info("\"" + elementName + "\" is enabled.");
            return true;
        }
        else {
            info("\"" + elementName + "\" is disabled.");
            return false;
        }
    }

    public static void clickOnElement(WebElement element, String elementName){

        try {
            WebDriverWait wait = new WebDriverWait(driver,20);
            wait.until(ExpectedConditions.elementToBeClickable(element));}
        catch (TimeoutException ex){
            info("\"" + elementName + "\" is not clickable.");
            clickHack(element, elementName);
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
            clickHack(element, elementName);
        }
        catch (TimeoutException e){
            stopLoad();
        }
        catch (StaleElementReferenceException e){
            warn("StaleElementReferenceException.");
            info("Click on \"" +elementName+"\".");
            element.click();
        }
        catch (WebDriverException e){
            error("WebDriverException" +e);
            clickHack(element, elementName);
        }
    }

    private static void clickHack(WebElement element, String elementName){
        boolean flag = true;
        int attempt = 0;

        while (flag && attempt<5){
            attempt++;
            try {
                info("\"" + elementName + "\" is hide by another element, move down.");
                scrollDown(500);
                moveToCoordinate(0, 0);
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

    public static void sendKeysClear(WebElement element, String elementName, String inputText){

        try {
            WaiterService.waitForElementVisible(element);
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

    public static int getWidth(WebElement element){
        return element.getSize().getWidth();
    }

    public static void moveToElement(WebElement element, String elementName) {

        try {
            WaiterService.waitForElementVisible(element);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).build().perform();
            sleep(1);
            info("\"" + elementName + "\" is active.");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\"" + elementName + "\" not found.");
        }
        catch (ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not visible.");
        }
    }

    public static void clickAndHoldElement(WebElement element, String elementName) {

        try {
            WaiterService.waitForElementVisible(element);
            new Actions(driver).clickAndHold(element).build().perform();
            sleep(1);
            info("\"" + elementName + "\" is hold.");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\"" + elementName + "\" not found.");
        }
        catch (ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not visible.");
        }
    }

    public static void selectDropBoxByText(WebElement element, String text){

        try{
            Select select = new Select(element);
            select.selectByVisibleText(text);
            info("Select \""+text+"\".");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\""+text+"\" is missing!");
        }
    }

    public static void deSelectDropBoxByText(WebElement element, String text){

        try {
            Select select = new Select(element);
            select.deselectByVisibleText(text);
            info("Deselect \""+text+"\".");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\""+text+"\" is missing!");
        }
    }

    public static WebElement getElement(By locator){
        WebElement element = null;
        boolean flag  = true;
        int attempt = 0;
        while (flag && attempt<20){
            attempt++;
            sleep(1);
            try {
                element =  driver.findElement(locator);
                flag = false;
            }
            catch (StaleElementReferenceException e){
                warn("StaleElementReferenceException for "+locator);
                element = null;
            }
            catch (NoSuchElementException e){
                warn("NoSuchElementException");
            }
            catch (Exception e){
                assertTrue(false, "Unknown exception.");
            }
        }
        return element;
    }

    public static void clear(WebElement element, String elementName){

        element.clear();
        info("\""+elementName+"\" field clear.");
    }

    public static void moveToCoordinate(int x, int y) {

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

    public static void sendKeysNative(WebElement element, String elementName, String inputText){
        try {

            Stream.of(inputText.toCharArray())
                    .forEach(c -> element.sendKeys(String.valueOf(c)));

            info("\"" + elementName + "\" input text: \"" + inputText + "\".");
        }
        catch (NoSuchElementException e){
            assertTrue(false, "\"" + elementName + "\" was not found on page after timeout.");
        }
        catch (ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not visible.");
        }
    }

	/**
     * Method cleared the field (BackSpace)
     *
     * @param element input on page
     */
    public static void clearFieldManual(WebElement element) {

        int countChar = getElementValue(element, element.toString()).length();
        int i = 0;
        while (i<countChar){
            i++;
            clickOnElement(element,"element");
            pressBackSpace(element);
        }
    }

    public static String getElementText(WebElement element, String elementName) {

        try {
            String text = element.getText();
            info("\"" + elementName +"\" content on page  - \"" + text + "\".");
            return text;
        }
        catch (NoSuchElementException | ElementNotVisibleException e){
            assertTrue(false, "\"" + elementName + "\" was not found on page after timeout.");
            throw new CustomException(e.toString());
        }
    }
}
