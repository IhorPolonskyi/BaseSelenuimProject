package service;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utility.Log;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static utility.services.WebDriverFactory.getDriverInstance;

/**
 * Created by igorp on 19/08/17.
 */
public class BaseTestCase {

    public WebDriver driver;

    @Parameters({"browser","server"})
    @BeforeMethod
    public void runBrowser(@Optional("chrome") String browserValue, @Optional("localhost") String gridValue) throws MalformedURLException {

        driver = getDriverInstance(browserValue, getGrid(gridValue));

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
        Log.info("Browser closed");
    }

    private String getGrid(String gridValue) {
        return "http://" + gridValue + ":4444/wd/hub";
    }
}
