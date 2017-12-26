package utility.services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utility.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 19.05.17.
 */
public class WebDriverFactory {

    public static WebDriver getDriverInstance(String browserName, String gridName) throws MalformedURLException{
        WebDriver driver;
        switch (browserName){
            case BrowserType.CHROME:
                driver = new RemoteWebDriver(new URL(gridName), getChromeDesiredCapabilities());
                break;
            case BrowserType.FIREFOX:
                driver = new RemoteWebDriver(new URL(gridName),  getFirefoxDesiredCapabilities());
                break;
            default:
                driver = new RemoteWebDriver(new URL(gridName), getChromeDesiredCapabilities());
                break;
        }
        return driver;
    }

    private static DesiredCapabilities getChromeDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();

        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setCapability(CapabilityType.SUPPORTS_APPLICATION_CACHE, true);
        desiredCapabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, addChromeOptions());

        return desiredCapabilities;
    }

    private static ChromeOptions addChromeOptions() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        Log.info("Maximize window.");

        return chromeOptions;
    }

    private static DesiredCapabilities getFirefoxDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setCapability(CapabilityType.SUPPORTS_APPLICATION_CACHE, true);
        desiredCapabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

        return desiredCapabilities;
    }

}
