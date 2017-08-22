package pageobjects;

import businessobjects.User;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utility.Constants;

import static utility.services.WaiterService.waitForElementClickable;
import static utility.services.WaiterService.waitPageLoader;
import static utility.services.WebElementService.clickOnElement;
import static utility.services.WebElementService.sendKeys;

/**
 * Created by igorp on 22/08/17.
 */
public class AuthPage {

    final WebDriver driver;

    public AuthPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath = "//input[@type='email']")
    public WebElement emailField;

    @FindBy(xpath = "//input[@type='password']")
    public WebElement passwordField;

    @FindBy(xpath = "//*[contains(@id,'continue')]//button")
    public WebElement continueButton;

    @FindBy(xpath = "//*[contains(@id,'login')]//button")
    public WebElement logInButton;

    @FindBy(className = "app-logo")
    public WebElement tmLogo;

    public void login(User user){

        waitForElementClickable(emailField, driver);
        sendKeys(emailField, "Email field", user.getEmail());
        clickOnElement(continueButton, "Continue button", driver);

        waitForElementClickable(passwordField, driver);
        sendKeys(passwordField, "Password field", user.getPassword());
        clickOnElement(logInButton, "Continue button", driver);

        //huck to get index page
        waitPageLoader("/downloads", driver);
        waitForElementClickable(tmLogo, driver);
        clickOnElement(tmLogo, "TM logo", driver);
        waitPageLoader(Constants.URL, driver);
    }



}
