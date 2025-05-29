package fravega.pages;

import actions.CommonActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class FravegaMainPage {

    CommonActions commonActions;
    private final static String URL = "https://www.fravega.com/";
    private static final String URL_DE_CUOTAS = "https://www.fravega.com/l/?formas-de-pago=12-cuotas-sin-interes%2C9-cuotas-sin-interes%2C3-cuotas-sin-interes%2C6-cuotas-sin-interes";

    private static final Logger logger = LoggerUtil.getLogger(FravegaMainPage.class);

    public FravegaMainPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[data-test-id='geo-modal-wrapper']")
    public WebElement modalPostalCode;
    @FindBy(css = "[data-test-id='close-modal-button']")
    public WebElement modalPostalCodeCloseButton;
    @FindBy(css = "input[name='keyword'][placeholder='Buscar productos']")
    public WebElement inputSearchProduct;
    @FindBy(css = "fieldset button[type='submit']")
    public WebElement searchProductsButton;


    public void openFravegaMainPage() {
        commonActions.openPage(URL);
    }
    public void openFravegaCuotasPage() {
        commonActions.openPage(URL_DE_CUOTAS);
        commonActions.waitForPageLoad();
        if (commonActions.isElementDisplayed(modalPostalCode)) {
            commonActions.clickElement(modalPostalCodeCloseButton);
        }
    }

    public void openMainPageAndHandleModal() {
        openFravegaMainPage();
        if (commonActions.isElementDisplayed(modalPostalCode)) {
            commonActions.clickElement(modalPostalCodeCloseButton);
        }
    }

    public void searchProduct(String product) {
        commonActions.typeText(inputSearchProduct, product);
        commonActions.clickElement(searchProductsButton, "Buscar productos");
        commonActions.waitForPageLoad();
    }


}
