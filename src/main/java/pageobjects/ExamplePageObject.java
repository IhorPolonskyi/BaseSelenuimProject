package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


import java.util.List;
import java.util.stream.Collectors;

import static utility.services.PressKeysService.pressEnter;
import static utility.services.WaiterService.waitForElementVisible;
import static utility.services.WebElementService.getElementText;
import static utility.services.WebElementService.sendKeysClear;

/**
 * Created by igorp on 19/08/17.
 */
public class ExamplePageObject {

    final WebDriver driver;

    public ExamplePageObject(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(id = "lst-ib")
    public WebElement searchField;

    @FindBy(css = "h3.r > a")
    public List<WebElement> resultsLinks;

    public void insertSearchRequest(String text) {
        sendKeysClear(searchField, "Search Field", text);
    }

    public void pressEnterOnSearchField() {
        pressEnter(searchField);
    }

    public List<String> getSearchResultsWebElementTexts() {
        return resultsLinks.stream()
                .map(element -> getElementText(element, "Search result text"))
                .collect(Collectors.toList());
    }

    public void waitForSearchResults() {
        waitForElementVisible(resultsLinks.get(0));
    }

}
