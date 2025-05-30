package fravega.tests;

import fravega.actions.CommonActions;
import fravega.base.ApplicationBaseTest;
import fravega.dataproviders.CuotasDataProvider;
import fravega.helpers.CuotaHelper;
import fravega.helpers.pojo.CuotasDisponibles;
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


    @Test(dataProvider = "injectAllCuotasAvailables", dataProviderClass = CuotasDataProvider.class)
    public void verificarPromocionesDeCuotasPorTarjetaYBanco(CuotasDisponibles inputDataCuotas) {
        WebDriver driver = getDriver();
        CommonActions commonActions = new CommonActions(driver);
        FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);
        ProductsSearchedPage productsSearchedPage = new ProductsSearchedPage(driver);
        CuotasModalPage cuotasModalPage = new CuotasModalPage(driver);
        SoftAssert softAssert = new SoftAssert();
        ProductPage productPage = new ProductPage(driver);

        try {
            fravegaMainPage.openSpecificCuotasPage(inputDataCuotas);
            productsSearchedPage.selectFirstProduct();
            Assert.assertTrue(productPage.isProductPageLoaded(), "La página del producto no se cargó correctamente.");

            final String FINANCED_FINAL_PRICE = productPage.getProductDiscountedPrice();
            List<TarjetaDeCredito> tarjetasPromocionadas = productPage.getAvailableCreditCardsForPromotion(inputDataCuotas);
            Assert.assertFalse(tarjetasPromocionadas.isEmpty(), "No hay tarjetas promocionadas con " + inputDataCuotas.asString() + " cuotas sin interés.");

            productPage.openPaymentModal();
            cuotasModalPage.openAllPaymentMethodsTab();

            logger.info("Verificando cuotas para las tarjetas promocionadas: {}", tarjetasPromocionadas);
            /*
            Iteramos sobre las tarjetas promocionadas y verificamos que cada una de ellas tenga las cuotas solicitadas.
             */

            for (TarjetaDeCredito tarjeta : tarjetasPromocionadas) {
                cuotasModalPage.selectCardByEnum(tarjeta);
                Map<String, List<CuotaInfo>> allCuotasAllBanks = cuotasModalPage.getAllCuotasForAllBanksForCreditCard();
                /* Podemos verificar que exista AL MENOS un banco que ofrezca las cuotas solicitadas, ya que puede ser que no todos los bancos ofrezcan las mismas cuotas para una tarjeta en particular.
                 */
                List<CuotaInfo> allCuotas = CuotaHelper.flattenCuotasForAllBanks(allCuotasAllBanks);
                Optional<CuotaInfo> cuotaOptional = CuotaHelper.filterCuotasByQuantity(allCuotas, inputDataCuotas);

                if (cuotaOptional.isEmpty()) {
                    softAssert.fail("No se encontro en ningun banco " + inputDataCuotas.asString() + " cuotas para la tarjeta " + tarjeta.name());
                    continue; // Si no hay cuotas, saltamos a la siguiente tarjeta
                }

                for (Map.Entry<String, List<CuotaInfo>> entry : allCuotasAllBanks.entrySet()) {
                    String bank = entry.getKey();
                    List<CuotaInfo> cuotas = entry.getValue();
                    Optional<CuotaInfo> cuota = CuotaHelper.filterCuotasByQuantity(cuotas, inputDataCuotas);

                    if(cuota.isEmpty()){
                        // Puede pasar que dada una tarjeta, para un banco en especifico no se ofrezcan las cuotas solicitadas, y no por eso debe fallar el test
                        continue;
                    }

                    // En caso de que si la ofrezcan, queremos verificar que el precio financiado sea el correcto y que no tenga intereses
                    softAssert.assertEquals(cuota.get().getTotalFinanced(), FINANCED_FINAL_PRICE,
                            "El precio financiado no coincide para " + tarjeta.name() + " en " + bank);
                    softAssert.assertTrue(cuota.get().hasNoInterests(),
                            "Cuota con interés para " + tarjeta.name() + " en " + bank + ": " + cuota.get().getInterest());
                }
            }

        } catch (Exception e) {
            logger.error("Error en test para " + inputDataCuotas + ": {}", e.getMessage(), e);
            softAssert.fail(e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }

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
