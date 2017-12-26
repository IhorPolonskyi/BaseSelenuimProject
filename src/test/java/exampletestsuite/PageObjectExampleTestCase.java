package exampletestsuite;

import org.testng.annotations.Test;
import pageobjects.ExamplePageObject;
import service.BaseTestCase;
import utility.Constants;

import static exampletestsuite.HelpMethods.searchRequestText;
import static exampletestsuite.HelpMethods.texts;
import static org.openqa.selenium.support.PageFactory.initElements;
import static utility.services.ManageUrlService.getDirectlyURL;
import static utility.services.ReportService.assertEquals;
import static utility.services.WaiterService.waitForElementVisible;
import static utility.services.WaiterService.waitPageLoader;

/**
 * Created by igorp on 21/08/17.
 */
public class PageObjectExampleTestCase extends BaseTestCase {

    @Test
    public void test_001_successSearch() {

        ExamplePageObject examplePageObject = initElements(driver, ExamplePageObject.class);

        //get google index page
        getDirectlyURL(Constants.URL, driver);
        waitPageLoader(Constants.URL, driver);

        //insert search request
        examplePageObject.insertSearchRequest(texts.get("searchText"));

        //press enter on search field
        examplePageObject.pressEnterOnSearchField();

        //wait for search results
        waitForElementVisible(examplePageObject.resultsLinks.get(0), driver);

        //assert that first search result is correct
        assertEquals(examplePageObject.getSearchResultsWebElementTexts().get(0), searchRequestText,
                "Incorrect text at second search result");

    }

}
