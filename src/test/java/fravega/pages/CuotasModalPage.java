package fravega.pages;

import actions.CommonActions;
import fravega.enums.TarjetaDeCredito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import utils.LoggerUtil;

import java.util.List;
import java.util.NoSuchElementException;

public class CuotasModalPage {

    CommonActions commonActions;
    private static final Logger logger = LoggerUtil.getLogger(CuotasModalPage.class);

    public CuotasModalPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[data-test-id='all-payment-methods']")
    public List<WebElement> allPaymentMethodsTab;
    @FindBy(xpath = "//div[@data-test-id='payment-method']")
    public WebElement paymentMethod;
    @FindBy(xpath = "//div[@data-test-id='payment-method']//img")
    public List<WebElement> creditCards;
    @FindBy(xpath = "//div[@class='tooltipContent']")
    public WebElement creditCardsContainer;
    @FindBy(xpath = "//div[@data-test-id='payment-method']/figure/img")
    public WebElement currentSelectedCard;

    public void openAllPaymentMethodsTab() {
        WebElement allPaymentMethodsTab = commonActions.getFirstVisibleElement(this.allPaymentMethodsTab, "Pestaña de todos los métodos de pago");
        commonActions.clickElement(allPaymentMethodsTab, "Pestaña de todos los métodos de pago");
    }

    public void selectCardByEnum(TarjetaDeCredito card) {
        commonActions.waitForElementDisplayed(paymentMethod);
        commonActions.clickElement(paymentMethod);
        commonActions.waitForElementDisplayed(creditCardsContainer);
        try{
            for (WebElement cardElement : creditCards) {
                String src = cardElement.getAttribute("src");
                if (src.equals(card.getSrc())) {
                    commonActions.clickElement(cardElement, "Tarjeta " + card.name());
                    return;
                }
            }
        } catch (NoSuchElementException e) {
            logger.error("No se encontró la tarjeta: " + card.name(), e);
        }
    }



}