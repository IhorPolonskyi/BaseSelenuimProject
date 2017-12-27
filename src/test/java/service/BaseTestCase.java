package service;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utility.Log;
import utility.services.CookiesService;
import utility.services.ManageUrlService;
import utility.services.WaiterService;
import utility.services.WebElementService;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static utility.services.WebDriverFactory.getDriverInstance;

/**
 * Created by igorp on 19/08/17.
 */
public class BaseTestCase {

    public WebDriver driver;

    public CookiesService cookiesService;
    public ManageUrlService manageUrlService;
    public WaiterService waiterService;
    public WebElementService webElementService;
    protected String testCaseName = this.getClass().getSimpleName();

    @Parameters({"browser","server"})
    @BeforeTest
    public void runBrowser(@Optional("chrome") String browserValue, @Optional("localhost") String gridValue) throws MalformedURLException {

        Log.info("TestCase: \"" + testCaseName + "\" started");

        driver = getDriverInstance(browserValue, getGrid(gridValue));

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

        getServiceConstructors();
    }

    @AfterTest
    public void closeBrowser() {
        driver.quit();
        Log.info("TestCase: \"" + testCaseName + "\" finished \n");
    }

    private String getGrid(String gridValue) {
        return "http://" + gridValue + ":4444/wd/hub";
    }

    private void getServiceConstructors(){
        cookiesService = new CookiesService(driver);
        manageUrlService = new ManageUrlService(driver);
        waiterService = new WaiterService(driver);
        webElementService = new WebElementService(driver);
    }

}
