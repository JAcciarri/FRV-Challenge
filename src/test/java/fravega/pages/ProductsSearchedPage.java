package fravega.pages;

import fravega.actions.CommonActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import fravega.utils.LoggerUtil;

import java.util.List;

public class ProductsSearchedPage {

    CommonActions commonActions;
    private static final Logger logger = LoggerUtil.getLogger(ProductsSearchedPage.class);

    public ProductsSearchedPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }


    @FindBy(css = "[data-test-id='results-list']")
    public WebElement productsListContainer;
    @FindBy(xpath = "//*[@data-test-id='results-list']//*[@data-test-id='result-item']")
    public List<WebElement> productsList;

    public List<WebElement> getProductsList() {
        commonActions.waitForElementDisplayed(productsListContainer);
        return productsList;
    }

    public void selectProduct(WebElement product) {
        WebElement clickableProduct = product.findElement(By.xpath(".//a"));
        commonActions.clickElement(clickableProduct, "Link del producto");
        commonActions.waitForPageLoad();
    }

    public String getProductOriginalPriceByIndex(int index) {
        WebElement product = productsList.get(index);
        WebElement priceElement = product.findElements(By.xpath(".//span[contains(text(), '$')]")).get(0);
        return commonActions.getTextFromElement(priceElement);
    }

    public String getProductDiscountedPriceByIndex(int index) {
        WebElement product = productsList.get(index);
        WebElement priceElement = product.findElements(By.xpath(".//span[contains(text(), '$')]")).get(1);
        return commonActions.getTextFromElement(priceElement);
    }

    public String getProductTitleByIndex(int index) {
        WebElement product = productsList.get(index);
        WebElement titleElement = product.findElement(By.xpath(".//span"));
        return commonActions.getTextFromElement(titleElement);
    }

}
