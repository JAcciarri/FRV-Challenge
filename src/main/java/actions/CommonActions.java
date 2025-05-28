package actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CommonActions {

    private WebDriver driver;
    private final static int MAX_TIMEOUT = 5;
    private final static int MAX_RETRIES = 3;
    private WebDriverWait wait;

    public CommonActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(MAX_TIMEOUT));
    }

    public void openPage(String url) {
        driver.get(url);
    }



}
