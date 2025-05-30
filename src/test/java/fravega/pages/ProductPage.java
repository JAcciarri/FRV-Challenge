package fravega.pages;

import fravega.actions.CommonActions;
import fravega.helpers.CuotaConstants;
import fravega.helpers.pojo.CuotasDisponibles;
import fravega.helpers.pojo.TarjetaDeCredito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import fravega.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;
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

    public boolean isProductPageLoaded() {
        commonActions.waitForPageLoad();
        WebElement productTitleElement = commonActions.getFirstVisibleElement(productTitle, "Product Title");
        return productTitleElement != null && commonActions.isElementDisplayed(productTitleElement);
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


    public List<TarjetaDeCredito> getAvailableCreditCardsForPromotion(){
        List<TarjetaDeCredito> availableCards = new ArrayList<>();

        try {
            WebElement rowCuotasAvailable = commonActions.findElement(CuotaConstants.DYNAMIC_PROMOTIONS_WITHOUT_INTEREST_6);
            if (rowCuotasAvailable == null) {
                logger.warn("No se encontraron tarjetas disponibles para promociones de 6 cuotas sin interés.");
                return Collections.emptyList();
            }

            String relativeLocator = CuotaConstants.ADD_REL_XPATH_DYNAMIC_CARDS_TO_BE_SELECTED;
            List<WebElement> cards = rowCuotasAvailable.findElements(By.xpath(relativeLocator));

            /* Iteramos sobre las tarjetas disponibles en la fila de cuotas sin interés
             * y las agregamos a la lista de tarjetas disponibles.
             */
            for (WebElement card : cards) {
                String src = card.getAttribute("src");

                if (src != null && !src.trim().isEmpty()) {
                    TarjetaDeCredito tarjeta = TarjetaDeCredito.fromSrc(src);
                    if (tarjeta != null) {
                        availableCards.add(tarjeta);
                    } else {
                        logger.warn("Tarjeta no reconocida {}", src);
                    }
                } else {
                    logger.warn("Se encontró un elemento de tarjeta sin atributo 'src' válido.");
                }
            }

            logger.info("Tarjetas detectadas para promo de 6 cuotas sin interés: {}", availableCards);
        } catch (Exception e) {
            logger.error("Error al obtener tarjetas disponibles para promoción 6 cuotas sin interés: {}", e.getMessage(), e);
        }

        return availableCards;
    }

}
