package fravega.pages;

import actions.CommonActions;
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
    @FindBy(css = "[data-test-id='button-open-modal-payment']")
    public List<WebElement> openPaymentModalButton;


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

    public void openPaymentModal() {
        WebElement paymentModalButton = commonActions.getFirstVisibleElement(openPaymentModalButton, "Open Payment Modal Button");
        if (paymentModalButton != null) {
            commonActions.clickElement(paymentModalButton, "Open Payment Modal Button");
            commonActions.waitForPageLoad();
        } else {
            logger.warn("Payment modal button is not visible or not found.");
        }
    }

}
