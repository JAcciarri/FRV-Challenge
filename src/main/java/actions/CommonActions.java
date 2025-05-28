package actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public class CommonActions {

    private final WebDriver driver;
    private final static int MAX_TIMEOUT = 5;
    private final FluentWait<WebDriver> wait;

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
        try {
            isDisplayed = wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Element not displayed within the timeout period: " + element.toString());
            return false;
        }
        return isDisplayed;
    }

    public void clickElement(WebElement element){
        waitForElementDisplayed(element);
        String elementDescription = element.getAttribute("data-test-id");
        elementDescription = (elementDescription != null && !elementDescription.isEmpty()) ? elementDescription : element.toString();
        System.out.println("Clicking on element: " + elementDescription);
        element.click();
    }

    public void clickElement(WebElement element, String description) {
        waitForElementDisplayed(element);
        System.out.println("Clicking on element: " + description);
        element.click();
    }

    public void typeText(WebElement element, String text) {
        waitForElementDisplayed(element);
        element.clear();
        System.out.println("Typing text: " + text);
        element.sendKeys(text);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        waitForElementDisplayed(element);
    }

    public String getTextFromElement(WebElement element) {
        waitForElementDisplayed(element);
        String text = element.getText();
        if(text == null || text.isEmpty()) {
            text = element.getAttribute("value");
        }
        System.out.println("Getting text from element: " + text);
        return text;
    }







}
