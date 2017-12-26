package utility.services;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static utility.Log.info;
import static utility.services.WaiterService.sleep;

public class PressKeysService {
    public static void pressEnter(WebElement element){
        element.sendKeys(Keys.ENTER);
        info("Press \"Enter\" key.");
    }

    public static void copyAndCut(WebElement element){
        element.sendKeys(Keys.CONTROL, Keys.chord("a"));
        element.sendKeys(Keys.CONTROL, Keys.chord("x"));
        info("Element is saved to buffer.");

    }

    public static void paste(WebElement element){
        element.sendKeys(Keys.CONTROL, Keys.chord("v"));
        info("Element is pasted from buffer.");

    }

    public static void pressTab(WebElement element){
        element.sendKeys(Keys.TAB);
        info("Press \"Tab\" key.");

    }

    public static void pressShiftTab(WebElement element){
        element.sendKeys(Keys.SHIFT, Keys.TAB);

    }

    public static void pressHome(WebElement element){
        element.sendKeys(Keys.HOME);
        info("Press \"Home\" key.");
    }

    public static void pressEnd(WebElement element){
        element.sendKeys(Keys.END);
        info("Press \"End\" key.");
    }

    public static void pressSpace(WebElement element){
        element.sendKeys(Keys.SPACE);
        info("Press \"Space\" key.");
    }

    public static void pressArrowDown(WebElement element){
        element.sendKeys(Keys.ARROW_DOWN);
        info("Press \"Arrow Down\" key.");
    }

    public static void pressArrowDown(WebDriver driver){
        driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_DOWN);
        info("Press \"Arrow Down\" key.");
    }

    public static void pressArrowUp(WebElement element){
        element.sendKeys(Keys.ARROW_UP);
        info("Press \"Arrow Up\" key.");
    }

    public static void pressBackSpace(WebElement element){
        element.sendKeys(Keys.BACK_SPACE);
        //Log.info("Press \"Back Space\" key.");
    }

    public static void pressPageDown(WebDriver driver){
        driver.findElement(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
        info("Press \"Page Down\" key.");
    }

    public static void selectAll(WebElement element){
        element.click();
        sleep(1);
        element.sendKeys(Keys.CONTROL, Keys.chord("a"));

    }

	/**
     * Method press key on active element (for chrome)
     * @param driver for switch to active element
     */
    public static void pressKeyActive(Keys key, WebDriver driver){
        driver.switchTo().activeElement().sendKeys(key);
        info("Press "+key.name()+" key.");
    }

}
