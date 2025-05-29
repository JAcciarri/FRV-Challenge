package fravega.pages;

import actions.CommonActions;
import fravega.enums.TarjetaDeCredito;
import fravega.pojo.CuotaConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import utils.LoggerUtil;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class CuotasModalPage {

    CommonActions commonActions;
    private static final Logger logger = LoggerUtil.getLogger(CuotasModalPage.class);

    public CuotasModalPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@class, 'ReactModal__Overlay')]")
    public WebElement cuotasModalOverlay;
    @FindBy(css = "[data-test-id='all-payment-methods']")
    public List<WebElement> allPaymentMethodsTab;
    @FindBy(xpath = "//div[@data-test-id='payment-method']")
    public WebElement selectPaymentMethod;
    @FindBy(xpath = "//div[@data-test-id='bank-select']")
    public WebElement selectBank;
    @FindBy(xpath = "//div[@data-test-id='payment-method']//img")
    public List<WebElement> creditCards;
    @FindBy(xpath = "//div[@class='tooltipContent']")
    public WebElement creditCardsContainer;
    @FindBy(xpath = "//div[@data-test-id='payment-method']/figure/img")
    public WebElement currentSelectedCard;

    public void openAllPaymentMethodsTab() {
        commonActions.waitForElementDisplayed(cuotasModalOverlay);
        WebElement paymentsTab = allPaymentMethodsTab.get(1);
        commonActions.clickElement(paymentsTab, "Pestaña de todos los métodos de pago");
    }

    public void selectCardByEnum(TarjetaDeCredito card) {
        commonActions.waitForElementDisplayed(selectPaymentMethod);
        commonActions.clickElement(selectPaymentMethod);
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

    public void iterateOverAllBanksForCreditCard() {
        commonActions.waitForElementDisplayed(selectBank);
        int index = 0;
        List<WebElement> banks;

        while (true) {
            WebElement selectBankDynamic = commonActions.findElement(CuotaConstants.DYNAMIC_SELECT_BANK);
            commonActions.clickElement(selectBankDynamic, "Abrir dropdown bancos");
            commonActions.waitForElementsToAppear(CuotaConstants.DYNAMIC_ALL_BANKS_FOR_A_CREDIT_CARD, Duration.ofSeconds(5));
            banks = commonActions.findElements(CuotaConstants.DYNAMIC_ALL_BANKS_FOR_A_CREDIT_CARD);

            if (index >= banks.size()) {
                break; // salimos cuando ya no quedan más bancos por iterar
            }

            WebElement currentBank = banks.get(index);
            String currentBankName = currentBank.findElement(By.xpath(".//p")).getText();
            commonActions.scrollToElement(currentBank);
            commonActions.clickElement(currentBank, "Banco: " + currentBankName);
            commonActions.waitForPageLoad();
            logger.info("________________________");
            logger.info("Pagos para el banco: {}", currentBankName);
            iterateOverAllPaymentsForBank();
            logger.info("________________________");

            index++;
        }
    }

    /**
     * Itera sobre todos los pagos disponibles para el banco seleccionado.
     * Extrae y muestra la información de cuotas, precio total financiado e intereses.
     */
    public void iterateOverAllPaymentsForBank(){
        commonActions.waitForElementsToAppear(CuotaConstants.DYNAMIC_ALL_PAYMENTS, Duration.ofSeconds(5));
        List<WebElement> payments = commonActions.findElements(CuotaConstants.DYNAMIC_ALL_PAYMENTS);
        for (WebElement payment : payments) {
            String cuota = payment.findElement(By.xpath(CuotaConstants.ADD_XPATH_ROW_CUOTAS)).getText();
            String totalPrice = payment.findElement(By.xpath(CuotaConstants.ADD_XPATH_ROW_TOTAL_FINANCED)).getText();
            String interests = payment.findElement(By.xpath(CuotaConstants.ADD_XPATH_ROW_INTEREST)).getText();
            logger.info("Cuotas: {}, Precio Total financiado: {}, Intereses: {}", cuota, totalPrice, interests);
        }
    }



}