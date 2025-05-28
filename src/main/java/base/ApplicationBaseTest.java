package base;

import org.openqa.selenium.WebDriver;
import webdriver.WebDriverFactory;

public class ApplicationBaseTest {

    public WebDriver getDriver() {
        return WebDriverFactory.createDriver("chrome");
    }

}
