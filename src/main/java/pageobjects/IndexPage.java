package pageobjects;

import businessobjects.User;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static utility.services.ManageUrlService.switchToLastWindowClose;
import static utility.services.WaiterService.waitForElementClickable;
import static utility.services.WebElementService.clickOnElement;

/**
 * Created by igorp on 19/08/17.
 */
public class IndexPage {

    private AuthPage authPage;

    final WebDriver driver;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(id = "header-signin-link")
    public WebElement accountLink;

    @FindBy(id = "user-avatar")
    public WebElement userAvatar;

    @FindBy(id = "header-signOut-lin")
    public WebElement signOutLink;

    @FindBy(id = "menu-favorites")
    public WebElement favorites;


    public void clickAccountLink(){
        clickOnElement(accountLink, "Account link", driver);
        switchToLastWindowClose(driver);
    }

    public void logOut(){
        clickOnElement(userAvatar, "Account menu", driver);
        waitForElementClickable(signOutLink, driver);
        clickOnElement(signOutLink, "Sign out link", driver);
    }

    public void logInFromIndexPage(User user){
        clickAccountLink();
        authPage.login(user);
    }
}
