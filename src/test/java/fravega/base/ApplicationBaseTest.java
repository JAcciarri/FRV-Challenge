package fravega.base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import webdriver.WebDriverFactory;
import org.testng.annotations.*;

public class ApplicationBaseTest {

    // ThreadLocal para manejar el WebDriver por hilos y permitir ejecucion paralela
    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return threadDriver.get();
    }

    /*protected WebDriver getDriver() {
        // Devuelve el driver creado por el metodo setup y sino devuelve uno nuevo de chrome con el Factory
        return Objects.requireNonNullElseGet(driver, () -> WebDriverFactory.createDriver("chrome", "local"));
    }*/

    @BeforeMethod(alwaysRun = true)
    public void testSetup(ITestContext context) {
        String browser = context.getCurrentXmlTest().getParameter("browser");
        String executionEnv = context.getCurrentXmlTest().getParameter("executionEnv");

        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
        }
        if (executionEnv == null || executionEnv.isEmpty()) {
            executionEnv = "local";
        }

        WebDriver driver = WebDriverFactory.createDriver(browser, executionEnv);
        threadDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = threadDriver.get();
        if (driver != null) {
            driver.quit();
            threadDriver.remove();
        }
    }

}

