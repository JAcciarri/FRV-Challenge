package tests;

import actions.CommonActions;
import base.ApplicationBaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import pages.FravegaMainPage;
import pages.ProductPage;
import pages.ProductsSearchedPage;
import utils.LoggerUtil;

import java.util.List;

public class CasoDeUsoHeladeraTest extends ApplicationBaseTest {

        private static final Logger logger = LoggerUtil.getLogger(CasoDeUsoHeladeraTest.class);

        @Test
        public void verificarCasoDeUsoHeladera(){
                WebDriver driver = getDriver();
                CommonActions commonActions = new CommonActions(driver);
                FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);
                ProductsSearchedPage productsSearchedPage = new ProductsSearchedPage(driver);
                ProductPage productPage = new ProductPage(driver);

                try {
                        fravegaMainPage.openMainPageAndHandleModal();
                        // ahora esta hardcodeado, pero los input se van a tomar del archivo json
                        fravegaMainPage.searchProduct("Heladera Samsung");
                        commonActions.waitForPageLoad();
                        // El metodo getProductsList en si ya tiene un wait que espera a el contenedor de productos
                        List<WebElement> products = productsSearchedPage.getProductsList();
                        if (products.isEmpty()) {
                                logger.info("No se encontraron productos para la búsqueda.");
                                return; // Salimos del test si no hay productos
                        }

                        logger.info("Cantidad de productos encontrados: " + products.size());

                        String originalPrice = productsSearchedPage.getProductOriginalPriceByIndex(1);
                        String discountedPrice = productsSearchedPage.getProductDiscountedPriceByIndex(1);
                        String productTitle = productsSearchedPage.getProductTitleByIndex(1);
                        logger.info("Titulo del segundo producto: " + productTitle);
                        logger.info("Precio original del segundo producto: " + originalPrice);
                        logger.info("Precio con descuento del segundo producto: " + discountedPrice);

                        // Ahora vamos a hacer click en el segundo producto de la lista y verificar todo
                        productsSearchedPage.selectProduct(products.get(1)); // click en el segundo producto de la lista
                        commonActions.waitForPageLoad();


                        // Los proximos pasos van a ser:
                        // 1. Verificar que el titulo del producto sea correcto
                        // 2. Verificar que el precio del producto sea correcto
                        // 3. Verificar que el boton de agregar al carrito este visible y enabled
                        // 4. Verificar que el boton de comprar ahora este visible y enabled
                        // 5. Luego pasar a la pagina de carrito y checkout y agregar mas verificaciones


                } catch (Exception e) {
                        logger.error("Error al abrir la pagina principal: " + e.getMessage());
                        logger.error("Estado del test: Falló al intentar realizar la búsqueda de productos.");
                } finally {
                        driver.quit();
                }



        }
}