package pages;

import actions.CommonActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProductPage {

    CommonActions commonActions;

    public ProductPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[data-test-id='product-title']")
    public WebElement productTitle;
    @FindBy(css = "[data-test-id='price-wrapper']")
    public WebElement productPrice;

    public String getProductTitle() {
        return commonActions.getTextFromElement(productTitle);
    }
    public String getProductPrice() {
        return commonActions.getTextFromElement(productPrice);
    }

}
