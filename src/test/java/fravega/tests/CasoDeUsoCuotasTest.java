package fravega.tests;

import actions.CommonActions;
import fravega.base.ApplicationBaseTest;
import fravega.pages.CheckoutPage;
import fravega.pages.FravegaMainPage;
import fravega.pages.ProductPage;
import fravega.pages.ProductsSearchedPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.LoggerUtil;

import java.util.List;

public class CasoDeUsoCuotasTest extends ApplicationBaseTest {
    private static final Logger logger = LoggerUtil.getLogger(CasoDeUsoCuotasTest.class);

    @Test
    public void verificarCasoDeUsoCuotas() {
        WebDriver driver = getDriver();
        CommonActions commonActions = new CommonActions(driver);
        FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);
        ProductsSearchedPage productsSearchedPage = new ProductsSearchedPage(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPage productPage = new ProductPage(driver);

        logger.info("Iniciando el Test de caso de uso Cuotas");

        try {
            fravegaMainPage.openFravegaCuotasPage();
            commonActions.waitForElementDisplayed(fravegaMainPage.inputSearchProduct);
            List<WebElement> products = productsSearchedPage.getProductsList();
            // Ahora voy a filtrar segun el test verifique por 3, 6 o 12 cuotas



        } catch (Exception e) {
            logger.error("Estado del test: Falló. {}", e.getMessage());
        } finally {

        }

        softAssert.assertAll();
    }

}
