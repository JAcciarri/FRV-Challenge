package actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public class CommonActions {

    private final WebDriver driver;
    private final static int MAX_TIMEOUT = 5;
    private final WebDriverWait wait;

    public CommonActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(MAX_TIMEOUT));
    }

    public void openPage(String url) {
        driver.get(url);
    }

    public void waitForPageLoad() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> pageLoadCondition = webDriver -> jse.executeScript("return document.readyState").equals("complete");
        wait.until(pageLoadCondition);
    }

    public boolean waitForElementDisplayed(WebElement element) {
        boolean isDisplayed;
        isDisplayed = wait.until(webDriver -> element.isDisplayed());
        return isDisplayed;
    }

    public void clickElement(WebElement element){
        waitForElementDisplayed(element);
        System.out.println("Clicking on element: " + element.toString());
        element.click();
    }

    public void typeText(WebElement element, String text) {
        waitForElementDisplayed(element);
        element.clear();
        System.out.println("Typing text: " + text);
        element.sendKeys(text);
    }







}
