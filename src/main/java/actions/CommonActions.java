package actions;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import utils.LoggerUtil;

import java.time.Duration;
import java.util.List;

public class CommonActions {

    private final WebDriver driver;
    private final static int MAX_TIMEOUT = 5;
    private final FluentWait<WebDriver> wait;
    private static final Logger logger = LoggerUtil.getLogger(CommonActions.class);

    public CommonActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(MAX_TIMEOUT))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);
    }

    public void openPage(String url) {
        driver.get(url);
    }

    public void waitForPageLoad() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> pageLoadCondition = webDriver -> jse.executeScript("return document.readyState").equals("complete");
        wait.until(pageLoadCondition);
    }

    public void waitForElementDisplayed(WebElement element) {
        try {
           wait.until(ExpectedConditions.visibilityOf(element));
           if( !element.isDisplayed() ) {
               logger.info("Element is not displayed: {}", element.toString());
           }

        } catch (TimeoutException e) {
            logger.warn("Element not displayed within the timeout period: {}", element.toString());
        }
    }

    public void clickElement(WebElement element){
        clickElement(element, "");
    }

    public void clickElement(WebElement element, String description) {
        waitForElementDisplayed(element);
        String desc = (description == null || description.isEmpty())
                ? element.getAttribute("data-test-id")
                : description;
        logger.info("Clicking on element: {}", desc);
        element.click();
    }

    public void typeText(WebElement element, String text) {
        waitForElementDisplayed(element);
        element.clear();
        logger.info("Typing text: {}", text);
        element.sendKeys(text);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element
        );
    }

    public String getTextFromElement(WebElement element) {
        waitForElementDisplayed(element);
        String text = element.getText();
        if(text == null || text.isEmpty()) {
            text = element.getAttribute("value");
        }
        return text;
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (TimeoutException e) {
            logger.info("Element not displayed: {}", element.toString());
            return false;
        }
    }

    public boolean isElementEnabled(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            logger.info("Element not enabled: {}", element.toString());
            return false;
        }
    }

    /* Actualmente en la pagina de fravega pude notar que hay mas de un elemento con el mismo selector para varios elementos
    Asi que decidi hacer este workaround para ayudar a los page object classes a obtener el primer elemento visible
    */
    public WebElement getFirstVisibleElement(List<WebElement> elements, String context) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        try {
            wait.until(driver -> !elements.isEmpty()); // espera que la lista no esté vacía
        } catch (Exception e) {
            logger.warn("Timeout esperando que {} tenga al menos un elemento.", context);
            return null;
        }

        for (WebElement element : elements) {
            try {
                scrollToElement(element);
                wait.until(ExpectedConditions.visibilityOf(element));
                if (element.isDisplayed()) {
                    logger.info("Elemento visible encontrado para {}: {}", context, element);
                    return element;
                }
            } catch (Exception e) {
                logger.debug("Elemento no visible para {}: {}", context, element);
            }
        }

        logger.warn("No se encontró ningún elemento visible para {}", context);
        return null;
    }

    public List<WebElement> findElements(String selector) {
        try {
            if(selector.startsWith("//")) {
                return driver.findElements(By.xpath(selector));
            } else {
                return driver.findElements(By.cssSelector(selector));
            }
        } catch (NoSuchElementException e) {
            logger.warn("No se encontraron elementos con el selector: {}", selector);
            return List.of();
        }
    }

    public void waitForDropdownOptionsToLoad(WebElement dropdown) {
        waitForDropdownOptionsToLoad(dropdown, 1);
    }

    public void waitForDropdownOptionsToLoad(WebElement dropdown, int expectedMinOptions) {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(driver -> {
            Select select = new Select(dropdown);
            return select.getOptions().size() >= expectedMinOptions;
        });
    }



}
