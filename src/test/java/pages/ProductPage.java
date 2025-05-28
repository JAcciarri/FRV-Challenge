package pages;

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
        logger.info("Init de elementos: tamaño de productTitle = {}", productTitle.size());
    }

    @FindBy(css = "[data-test-id='product-info'] [data-test-id='product-title']")
    public List<WebElement> productTitle;
    @FindBy(xpath = "//div[@data-test-id='price-wrapper']//span[contains(text(),'$')]")
    public List<WebElement> productPrices;
    @FindBy(css = "[data-test-id='product-buy-button']")
    public WebElement buyButton;
    @FindBy(xpath = "//*[@data-test-id='product-info']//button[text()='Agregar al carrito']")
    public WebElement addToCartButton;
    @FindBy(css = "[data-test-id='carousel-product']")
    public List<WebElement> productCarousel;



    public void waitForProductPageToLoad() {
        commonActions.waitForPageLoad();
        WebElement visibleCarousel = commonActions.getFirstVisibleElement(productCarousel, "Product Carousel");
        commonActions.waitForElementDisplayed(visibleCarousel);
    }

    public String getProductOriginalPrice() {
        WebElement productPrice = productPrices.get(2);
        return commonActions.getTextFromElement(productPrice);
    }
    public String getProductDiscountedPrice() {
        WebElement discountedPriceElement = productPrices.get(3);
        return commonActions.getTextFromElement(discountedPriceElement);
    }

    public void buyProduct() {
        commonActions.scrollToElement(buyButton);
        commonActions.clickElement(buyButton, "Buy Product Button");
        commonActions.waitForPageLoad();
    }
    public void addToCart() {
        commonActions.clickElement(addToCartButton, "Add to Cart Button");
    }

    public String getProductTitle() {
        WebElement element = commonActions.getFirstVisibleElement(productTitle, "Titulo del producto");
        return element != null ? commonActions.getTextFromElement(element) : "";
    }


    }
