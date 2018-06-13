package utility.services;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static utility.Log.info;
import static utility.Log.warn;
import static utility.services.ReportService.assertTrue;
import static utility.services.WaiterService.sleep;
import static utility.services.WebElementService.moveToCoordinate;

public class ManageUrlService {

    private static WebDriver driver;
    public ManageUrlService(WebDriver driver){
        this.driver = driver;
    }

    public static void getDirectlyURL(String url) {
        driver.getCurrentUrl();
        info("Navigate to \""+url+"\".");
        try {
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.get(url);

            info("Navigate to \""+url+"\" finished.");
            moveToCoordinate(0,0);
        }
        catch (TimeoutException e){
            stopLoad();
        }
    }

    public static void refreshPage(){
        try {
            driver.navigate().refresh();
            info("Page was refreshed.");
        }
        catch (WebDriverException e){
            stopLoad();
        }
    }

    public static String getCurrentURL(){
        info("Current URL:"+driver.getCurrentUrl());
        return driver.getCurrentUrl();
    }

    public static void switchToFrame(String frameId){
        int attempt=0;
        boolean flag = true;
        while (flag && attempt<10){
            attempt++;
            try {
                driver.switchTo().frame(frameId);
                info("Switched to \""+frameId+"\" frame.");
                flag = false;
            }
            catch (NoSuchFrameException e){
                warn("NoSuchFrameException "+frameId);
            }
        }
    }

    public static void switchToFrame(int frameIndex){
        List<WebElement> frames = driver.findElements(By.tagName("frame"));
        int attemptCounter = 0;
        while (frames.size()<=frameIndex){
            frames = driver.findElements(By.tagName("frame"));
            sleep(1);
            attemptCounter++;
            if (attemptCounter == 10){
                assertTrue(false, "Invalid index of frame");
            }
        }

        driver.switchTo().frame(frames.get(frameIndex));
        info("Switch to "+frameIndex+"-index frame.");

    }

    public static void switchToIframe(int frameIndex){
        List<WebElement> frames = driver.findElements(By.tagName("iframe"));
        int attemptCounter = 0;
        while (frames.size()<=frameIndex){
            frames = driver.findElements(By.tagName("iframe"));
            sleep(1);
            attemptCounter++;
            if (attemptCounter == 10){
                assertTrue(false,"Invalid index of iframe");
            }
        }

        driver.switchTo().frame(frames.get(frameIndex));
        info("Switch to "+frameIndex+"-index iframe.");

    }

    public static String getWindow(){
        return driver.getWindowHandle();
    }

    public static void switchToWindow(String windowName){
        info("Switch to \""+windowName+"\" window.");
        driver.switchTo().window(windowName);
    }

    public static void switchToWindow(){
        for (String win:driver.getWindowHandles()){
            driver.switchTo().window(win);
        }
        info("Switch to another window.");
        driver.manage().window().maximize();
        info("Maximize window.");
    }

    public static void switchToLastWindow(){
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

    public static void switchToLastWindowClose(){
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


    public static int countWindows(){
        int counter = 0;
        for (String win :driver.getWindowHandles()){
            counter++;
        }
        info("Opened windows = "+counter);
        return counter;
    }

    public static void closeWindow(){
        driver.close();
        info("Close current window.");
    }

    public  static void openNewWindow(){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("window.open('');");
        info("Open new window.");
    }

    public static void navigateBack (){
        try {
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.navigate().back();
            info("Returned to the previous page.");
        }
        catch (TimeoutException e){
            stopLoad();
        }

    }

    public static String getTitle(){
        info("Page title: \""+driver.getTitle()+"\".");
        return driver.getTitle();
    }

    public  static String getHeading(String tag){
        String heading = driver.findElement(By.tagName(tag)).getText();
        info(tag+" heading text: "+heading);
        return heading;
    }


    public static void scrollDown(){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
    }

    public static void switchToContent(){
        driver.switchTo().defaultContent();
        info("Switch to default content.");
    }

    public static void stopLoad(){
        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        info("Timeout on loading page \""+driver.getCurrentUrl()+"\".");
    }

    public static void resizeWindow(Dimension dimension){
        driver.manage().window().setSize(dimension);
        info("Resize window to "+dimension+" size.");
    }

    public static void scrollUp(int px){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("window.scrollBy(0,-"+px+")", "");
    }

    public static void switchToIframe(WebElement frame){
        driver.switchTo().frame(frame);
        info("Switch to "+frame+" iframe.");

    }

    public static void scrollDown(int px){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("window.scrollBy(0,"+px+")", "");
    }

}
