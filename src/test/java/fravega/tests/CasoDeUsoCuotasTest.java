package fravega.tests;

import fravega.actions.CommonActions;
import fravega.base.ApplicationBaseTest;
import fravega.helpers.pojo.TarjetaDeCredito;
import fravega.pages.*;
import fravega.helpers.pojo.CuotaInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import fravega.utils.LoggerUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CasoDeUsoCuotasTest extends ApplicationBaseTest {
    private static final Logger logger = LoggerUtil.getLogger(CasoDeUsoCuotasTest.class);

    @Test
    public void verificarCasoDeUsoCuotas() {
        WebDriver driver = getDriver();
        CommonActions commonActions = new CommonActions(driver);
        FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);
        ProductsSearchedPage productsSearchedPage = new ProductsSearchedPage(driver);
        CuotasModalPage cuotasModalPage = new CuotasModalPage(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPage productPage = new ProductPage(driver);

        logger.info("Iniciando el Test de caso de uso Cuotas");

        try {
            fravegaMainPage.openFravegaCuotasPage();
            commonActions.waitForElementDisplayed(fravegaMainPage.inputSearchProduct);
            List<WebElement> products = productsSearchedPage.getProductsList();
            if(products.isEmpty()){
                Assert.fail("No se encontraron productos en la búsqueda");
            }
            // Aca puedo filtrar los productos segun el test verifique por 3, 6 o 12 cuotas con el enum que cree
            productsSearchedPage.selectProduct(products.get(0));
            productPage.openPaymentModal();
            cuotasModalPage.openAllPaymentMethodsTab();

            logger.info("Seleccionando tarjeta visa e iterando sobre bancos disponibles");
            cuotasModalPage.selectCardByEnum(TarjetaDeCredito.VISA);
            Map<String, List<CuotaInfo>> results = cuotasModalPage.getAllCuotasForAllBanksForCreditCard();

            logger.info("Resultados obtenidos: {}", results);

            results.forEach((bank, cuotas) -> {
                Optional<CuotaInfo> tresCuotas = cuotas.stream()
                        .filter(c -> c.getCuotas().contains("3"))
                        .findFirst();
                softAssert.assertTrue(tresCuotas.isPresent(), "No se encontraron cuotas de 3 para el banco " + bank);
                softAssert.assertTrue(tresCuotas.get().hasNoInterests(),
                        "La opción de 3 cuotas tiene interés: " + tresCuotas.get().getInterest() + " para el banco " + bank);
            });



        } catch (Exception e) {
            logger.error("Estado del test: Falló. {}", e.getMessage());
            softAssert.fail(e.getMessage());
        } finally {

        }

        softAssert.assertAll();
    }

}
