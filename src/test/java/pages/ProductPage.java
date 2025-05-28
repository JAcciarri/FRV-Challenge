package pages;

import actions.CommonActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import utils.LoggerUtil;

import java.util.List;

public class ProductPage {

    CommonActions commonActions;
    private static final Logger logger = LoggerUtil.getLogger(ProductPage.class);

    public ProductPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[data-test-id='product-info'] [data-test-id='product-title']")
    public List<WebElement> productTitle;
    @FindBy(xpath = "//div[@data-test-id='price-wrapper']//span[contains(text(),'$')]")
    public List<WebElement> productPrices;
    @FindBy(css = "[data-test-id='product-buy-button']")
    public List<WebElement> buyButton;
    @FindBy(xpath = "//*[@data-test-id='product-info']//button[text()='Agregar al carrito']")
    public List<WebElement> addToCartButton;
    @FindBy(css = "[data-test-id='carousel-product']")
    public List<WebElement> productCarousel;




    public String getProductOriginalPrice() {
        WebElement productPrice = productPrices.get(2);
        return commonActions.getTextFromElement(productPrice);
    }
    public String getProductDiscountedPrice() {
        WebElement discountedPriceElement = productPrices.get(3);
        return commonActions.getTextFromElement(discountedPriceElement);
    }

    public void buyProduct() {
        WebElement buyElement = commonActions.getFirstVisibleElement(buyButton, "Buy Product Button");
        commonActions.clickElement(buyElement, "Buy Product Button");
        commonActions.waitForPageLoad();
    }
    public void addToCart() {
        commonActions.clickElement(addToCartButton.get(1), "Add to Cart Button");
    }

    public boolean isBuyButtonEnabled() {
        WebElement buyElement = commonActions.getFirstVisibleElement(buyButton, "Buy Button");
        return buyElement != null  && commonActions.isElementEnabled(buyElement);
    }

    public String getProductTitle() {
        WebElement element = commonActions.getFirstVisibleElement(productTitle, "Titulo del producto");
        return element != null ? commonActions.getTextFromElement(element) : "";
    }

    }
