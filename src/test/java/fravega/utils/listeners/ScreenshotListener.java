package fravega.utils.listeners;

import fravega.base.ApplicationBaseTest;
import io.qameta.allure.Attachment;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import webdriver.WebDriverFactory;

public class ScreenshotListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            WebDriver driver = ApplicationBaseTest.getDriver();
            captureScreenshot(driver);
        }
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public byte[] captureScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
