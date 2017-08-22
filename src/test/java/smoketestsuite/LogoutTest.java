package smoketestsuite;

import org.testng.annotations.Test;

import static utility.services.ReportService.assertTrue;
import static utility.services.WaiterService.waitForElementDisappear;
import static utility.services.WebElementService.elementIsDisplayed;

/**
 * Created by igorp on 22/08/17.
 */
public class LogoutTest extends LoginTest {

    @Test
    public void logoutTest() {

        indexPage.logOut();

        waitForElementDisappear(indexPage.userAvatar, "User avatar");
        assertTrue(elementIsDisplayed(indexPage.accountLink, "Account Link"),
                "Account link is not visible");
    }
}
