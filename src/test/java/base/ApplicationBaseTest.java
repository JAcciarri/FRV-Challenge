package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import webdriver.WebDriverFactory;
import org.testng.annotations.*;

import java.util.Objects;

public class ApplicationBaseTest {

    protected WebDriver driver;

    protected WebDriver getDriver() {
        // Devuelve el driver creado por el metodo setup y sino devuelve uno nuevo de chrome con el Factory
        return Objects.requireNonNullElseGet(driver, () -> WebDriverFactory.createDriver("chrome"));
    }


    @BeforeTest(alwaysRun = true)
    public void setUp(ITestContext context) {
        String browser = context.getCurrentXmlTest().getParameter("browser");
        if (browser == null || browser.isEmpty()) {
            browser = "chrome"; // Default browser
        }
         driver = WebDriverFactory.createDriver(browser);
    }

    @AfterTest
    public void tearDown() {
        if(driver != null) {
            driver.close();
        }
    }
    @AfterSuite
    public void afterClass() {
        if (driver != null) {
            driver.quit();
        }
    }
}

