package fravega.pages;

import actions.CommonActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class CheckoutPage {

    CommonActions commonActions;
    private static final Logger logger = LoggerUtil.getLogger(CheckoutPage.class);

    public CheckoutPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//h1[text()='Mi carrito']")
    public WebElement checkoutTitle;
    @FindBy(xpath = "//div[contains(@style, 'display: flex')]/div/div")
    public WebElement productTitleInCheckout;
    @FindBy(xpath = "//div[contains(text(), 'Total')]/following-sibling::div")
    public WebElement totalPriceInCheckout;
    @FindBy(xpath = "//span[contains(@class, 'num')]")
    public WebElement amountOfProductsInCheckout;

    public void waitForCheckoutPageToLoad() {
        commonActions.waitForPageLoad();
        commonActions.waitForElementDisplayed(checkoutTitle);
    }

    public String getProductTitleInCheckout() {
        return commonActions.getTextFromElement(productTitleInCheckout);
    }
    public String getTotalPriceInCheckout() {
        String totalPrice = commonActions.getTextFromElement(totalPriceInCheckout);
        return sanitize(totalPrice);
    }
    public String getAmountOfProductsInCheckout() {
        String amountOfProducts = commonActions.getTextFromElement(amountOfProductsInCheckout);
        return sanitize(amountOfProducts);
    }
    /*
    Se creo este metodo porque en algunas partes de la pagina el precio o el titulo agregan espacios antes o al final o en el medio.
     */
    private String sanitize(String text) {
        return text.trim().replaceAll(" ", "");
    }


}
