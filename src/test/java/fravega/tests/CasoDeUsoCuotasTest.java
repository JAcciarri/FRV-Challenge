package fravega.tests;

import fravega.actions.CommonActions;
import fravega.base.ApplicationBaseTest;
import fravega.dataproviders.CuotasDataProvider;
import fravega.dataproviders.CuotasProductsJSONProvider;
import fravega.utils.helpers.CuotaHelper;
import fravega.pojo.CuotasDisponibles;
import fravega.pojo.TarjetaDeCredito;
import fravega.pages.*;
import fravega.pojo.CuotaInfo;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import fravega.utils.LoggerUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import io.qameta.allure.*;

@Epic("Validación de cuotas")
@Feature("Validaciones de promociones y bancos")

public class CasoDeUsoCuotasTest extends ApplicationBaseTest {
    private static final Logger logger = LoggerUtil.getLogger(CasoDeUsoCuotasTest.class);


    @Test(dataProvider = "injectAllCuotasAvailables", dataProviderClass = CuotasDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valida que las promociones de cuotas sin interés se apliquen correctamente por tarjeta y banco")

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
            Assert.assertFalse(tarjetasPromocionadas.isEmpty(),
                    "No hay tarjetas promocionadas con " + inputDataCuotas.asString() + " cuotas sin interés.");

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
                    softAssert.assertEquals(
                            cuota.get().getTotalFinanced(),
                            FINANCED_FINAL_PRICE,
                            "El precio financiado no coincide para " + tarjeta.name() + " en " + bank);
                    softAssert.assertTrue(
                            cuota.get().hasNoInterests(),
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


    @Test(dataProvider = "cuotasHeladerasDataProvider", dataProviderClass = CuotasProductsJSONProvider.class)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica la disponibilidad de cuotas por tipo de producto")
    public void verificarCuotasPorProducto(Map<String, String> data) {
        WebDriver driver = getDriver();
        CommonActions commonActions = new CommonActions(driver);
        FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);
        ProductsSearchedPage productsSearchedPage = new ProductsSearchedPage(driver);
        ProductPage productPage = new ProductPage(driver);
        CuotasModalPage cuotasModalPage = new CuotasModalPage(driver);
        SoftAssert softAssert = new SoftAssert();
        String inputProductSearch = data.get("product");
        CuotasDisponibles cuotas = CuotasDisponibles.valueOf(data.get("cuotas"));


        try {
            // Buscar producto
            fravegaMainPage.openMainPageAndHandleModal();
            fravegaMainPage.searchProduct(inputProductSearch);
            productsSearchedPage.clickOnCuotasToFilterProducts(cuotas);
            productsSearchedPage.selectFirstProductWithDesiredCuotas(cuotas);

            Assert.assertTrue(productPage.isProductPageLoaded(), "No se cargó la página del producto.");
            String precioFinanciado = productPage.getProductDiscountedPrice();

            List<TarjetaDeCredito> tarjetasPromo = productPage.getAvailableCreditCardsForPromotion(cuotas);
            Assert.assertFalse(tarjetasPromo.isEmpty(),
                    "No hay tarjetas promocionadas para " + cuotas.asString());

            productPage.openPaymentModal();
            cuotasModalPage.openAllPaymentMethodsTab();

            for (TarjetaDeCredito tarjeta : tarjetasPromo) {
                cuotasModalPage.selectCardByEnum(tarjeta);
                Map<String, List<CuotaInfo>> allCuotas = cuotasModalPage.getAllCuotasForAllBanksForCreditCard();

                for (Map.Entry<String, List<CuotaInfo>> entry : allCuotas.entrySet()) {
                    Optional<CuotaInfo> cuota = CuotaHelper.filterCuotasByQuantity(entry.getValue(), cuotas);
                    if (cuota.isEmpty()) continue;

                    softAssert.assertEquals(
                            cuota.get().getTotalFinanced(),
                            precioFinanciado,
                            "Precio financiado incorrecto para " + tarjeta.name() + " en " + entry.getKey()
                    );

                    softAssert.assertTrue(
                            cuota.get().hasNoInterests(),
                            "Interés no esperado en cuota para " + tarjeta.name() + " en " + entry.getKey()
                    );
                }
            }

        } catch (Exception e) {
            logger.error("Error en test para '{}', {} cuotas: {}", inputProductSearch, cuotas.asString(), e.getMessage(), e);
            softAssert.fail(e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }

}
