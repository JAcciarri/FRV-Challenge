package pages;

import actions.CommonActions;
import org.openqa.selenium.WebDriver;

public class FravegaMainPage {

    private WebDriver driver;
    CommonActions commonActions;
    private final static String URL = "https://www.fravega.com/";

    public FravegaMainPage(WebDriver driver) {
        this.driver = driver;
        this.commonActions = new CommonActions(driver);
        driver.get(URL);
    }

    public void openFravegaMainPage() {
        commonActions.openPage(URL);
    }


}
