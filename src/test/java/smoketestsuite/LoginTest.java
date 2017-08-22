package smoketestsuite;

import businessobjects.User;
import org.testng.annotations.Test;
import pageobjects.IndexPage;
import utility.Constants;

import static org.openqa.selenium.support.PageFactory.initElements;
import static utility.services.CookiesService.getCookieValue;
import static utility.services.ManageUrlService.getURL;
import static utility.services.ReportService.assertEquals;
import static utility.services.ReportService.assertTrue;
import static utility.services.WaiterService.waitCssLoad;
import static utility.services.WaiterService.waitForElementVisible;
import static utility.services.WebElementService.elementIsDisplayed;

/**
 * Created by igorp on 21/08/17.
 */
public class LoginTest extends BaseTest {
    User user = new User("properties/defaultUser.properties");
    IndexPage indexPage = initElements(driver, IndexPage.class);

    @Test
    public void loginTest() {

        //go to tm.com
        getURL(Constants.URL, driver);
        waitCssLoad(driver);

        //login and go back to index page
        indexPage.logInFromIndexPage(user);

        //check that user logged in - user avatar and cookie lgn value
        waitCssLoad(driver);
        waitForElementVisible(indexPage.userAvatar, driver);
        assertTrue(elementIsDisplayed(indexPage.userAvatar, "User avatar"), "User avatar is not visible");

        assertEquals(getCookieValue("lgn", driver), user.getEmail(), "Incorrect cookie lgn value");
    }

}
