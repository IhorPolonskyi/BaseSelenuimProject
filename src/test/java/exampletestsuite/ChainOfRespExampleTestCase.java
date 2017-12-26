package exampletestsuite;

import businessobjects.User;
import org.testng.annotations.Test;
import pageobjects.GoogleSearchPageChainOfResp;
import service.BaseTestCase;
import utility.Constants;

import static exampletestsuite.HelpMethods.searchRequestText;
import static org.openqa.selenium.support.PageFactory.initElements;
import static utility.services.ReportService.assertEquals;

/**
 * Created by igorp on 21/08/17.
 */
public class ChainOfRespExampleTestCase extends BaseTestCase {

    @Test
    public void test_001_successSearch() {

        GoogleSearchPageChainOfResp googleSearchPageChainOfResp = initElements(driver, GoogleSearchPageChainOfResp.class)
                .getUrl(Constants.URL)
                .waitPageLoad(Constants.URL)
                .insertSearchRequest("text")
                .pressEnterOnSearchField()
                .waitForSearchResults();

        assertEquals(googleSearchPageChainOfResp.getSearchResultsWebElementTexts().get(0), searchRequestText,
                "Incorrect text at second search result");

    }

}

