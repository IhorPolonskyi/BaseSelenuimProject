package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.LinkedList;
import java.util.List;

import static utility.services.ManageUrlService.getDirectlyURL;
import static utility.services.PressKeysService.pressEnter;
import static utility.services.WaiterService.waitForElementVisible;
import static utility.services.WaiterService.waitPageLoader;
import static utility.services.WebElementService.getElementText;
import static utility.services.WebElementService.sendKeysClear;

/**
 * Created by igorp on 19/08/17.
 */
public class GoogleSearchPageChainOfResp{

    protected WebDriver driver;

    public GoogleSearchPageChainOfResp(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "lst-ib")
    private WebElement searchField;

    @FindBy(css = "h3.r > a")
    private List<WebElement> resultsLinks;

    public GoogleSearchPageChainOfResp getUrl(String url) {
        getDirectlyURL(url);
        return this;
    }

    public GoogleSearchPageChainOfResp insertSearchRequest(String text) {
        sendKeysClear(searchField, "Search Field", text);
        return this;
    }

    public GoogleSearchPageChainOfResp pressEnterOnSearchField() {
        pressEnter(searchField);
        return this;
    }

    public List<String> getSearchResultsWebElementTexts() {
        List<String> list = new LinkedList<>();
        resultsLinks.forEach(element ->{
            list.add(getElementText(element, "Search result text"));
        });
        return list;
    }

    public GoogleSearchPageChainOfResp waitForSearchResults() {
        waitForElementVisible(resultsLinks.get(0));
        return this;
    }

    public GoogleSearchPageChainOfResp waitPageLoad(String url){
        waitPageLoader(url);
        return this;
    }
}
