package smoketestsuite;

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
public class BaseTest {

    public WebDriver driver;

    @Parameters({"browser", "node"})
    @BeforeMethod
    public void runBrowser(@Optional("chrome") String browserValue,
                           @Optional("http://localhost:4444/wd/hub") String nodeValue) throws MalformedURLException {

        driver = getDriverInstance(browserValue, nodeValue);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

    }


    @AfterMethod
    public void closeBrowser() {
        driver.quit();
        Log.info("Browser closed");
    }
}
