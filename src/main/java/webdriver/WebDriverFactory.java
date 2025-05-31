package webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import fravega.utils.LoggerUtil;

public class WebDriverFactory {

    private static final Logger logger = LoggerUtil.getLogger(WebDriverFactory.class);

    public static WebDriver createDriver(String browser, String executionEnv) {
        boolean jenkinsRun = "jenkins".equalsIgnoreCase(executionEnv);
        if(jenkinsRun) {logger.info("Running in Jenkins environment, setting browser to headless mode.");}

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (jenkinsRun) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                }
                return new ChromeDriver(chromeOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (jenkinsRun) {
                    firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--window-size=1920,1080");
                } else {
                    firefoxOptions.addArguments("--start-maximized");
                }
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (jenkinsRun) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--window-size=1920,1080");
                } else {
                    edgeOptions.addArguments("--start-maximized");
                }
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Navegador no soportado: " + browser);
        }
    }
}
