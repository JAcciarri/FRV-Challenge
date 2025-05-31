package fravega.base;

import fravega.utils.listeners.ScreenshotListener;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import webdriver.WebDriverFactory;
import org.testng.annotations.*;

@Listeners({ScreenshotListener.class})
public class ApplicationBaseTest {

    // ThreadLocal para manejar el WebDriver por hilos y permitir ejecucion paralela
    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(ApplicationBaseTest.class);

    public static WebDriver getDriver() {
        WebDriver driver = threadDriver.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver no ha sido inicializado para el hilo actual.");
        }
        return driver;
    }


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
    public void tearDown(ITestResult result) {
        WebDriver driver = threadDriver.get();
        if (driver != null) {
            driver.quit();
            threadDriver.remove();
        }
    }

}

