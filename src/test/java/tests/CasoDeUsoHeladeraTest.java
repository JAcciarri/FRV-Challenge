package tests;

import actions.CommonActions;
import base.ApplicationBaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.FravegaMainPage;
import pages.ProductPage;
import pages.ProductsSearchedPage;
import utils.LoggerUtil;

import java.util.List;

public class CasoDeUsoHeladeraTest extends ApplicationBaseTest {

        private static final Logger logger = LoggerUtil.getLogger(CasoDeUsoHeladeraTest.class);

        @Parameters({"keywordSearch"})
        @Test
        public void verificarCasoDeUsoHeladera(String inputDataSearch) {
                WebDriver driver = getDriver();
                CommonActions commonActions = new CommonActions(driver);
                FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);
                ProductsSearchedPage productsSearchedPage = new ProductsSearchedPage(driver);
                SoftAssert softAssert = new SoftAssert();
                ProductPage productPage = new ProductPage(driver);

                logger.info("Iniciando el test de caso de uso heladera");

                try {
                        fravegaMainPage.openMainPageAndHandleModal();
                        softAssert.assertTrue(commonActions.isElementDisplayed(fravegaMainPage.inputSearchProduct),
                                "El campo de búsqueda no está visible en la página principal.");

                        fravegaMainPage.searchProduct(inputDataSearch);
                        commonActions.waitForPageLoad();
                        List<WebElement> products = productsSearchedPage.getProductsList();
                        final int INDEX_DESIRED_PRODUCT = 1; // Segundo producto de la lista
                        if (products.isEmpty()) {
                                softAssert.fail("No se encontraron productos para esa busqueda"); // Salimos del test si no hay productos
                        }

                        logger.info("Cantidad de productos encontrados: " + products.size());

                        String originalPrice = productsSearchedPage.getProductOriginalPriceByIndex(INDEX_DESIRED_PRODUCT);
                        String discountedPrice = productsSearchedPage.getProductDiscountedPriceByIndex(INDEX_DESIRED_PRODUCT);
                        String productTitle = productsSearchedPage.getProductTitleByIndex(INDEX_DESIRED_PRODUCT);
                        logger.info("Titulo del segundo producto: " + productTitle);
                        logger.info("Precio original del segundo producto: " + originalPrice);
                        logger.info("Precio con descuento del segundo producto: " + discountedPrice);

                        // Ahora vamos a hacer click en el segundo producto de la lista y verificar
                        productsSearchedPage.selectProduct(products.get(INDEX_DESIRED_PRODUCT));
                        productPage.waitForProductPageToLoad();

                        softAssert.assertEquals(productPage.getProductTitle(), productTitle,
                                "El título del producto no coincide con el esperado.");
                        softAssert.assertEquals(productPage.getProductDiscountedPrice(), discountedPrice,
                                "El precio con descuento del producto no coincide con el esperado.");
                        softAssert.assertEquals(productPage.getProductOriginalPrice(), originalPrice,
                                "El precio original del producto no coincide con el esperado.");

                        // SE ASUME QUE SI EL BOTON DE COMPRAR Y AGREGAR AL CARRITO ESTAN VISIBLE Y ENABLED, EL PRODUCTO ESTA DISPONIBLE
                        if(commonActions.isElementDisplayed(productPage.addToCartButton) &&
                                commonActions.isElementEnabled(productPage.addToCartButton)) {
                                logger.info("El botón de añadir al carrito ahora está visible y habilitado.");
                        } else {
                                softAssert.fail("El botón de añadir al carrito no está visible o habilitado.");
                        }


                        productPage.buyProduct();


                        // Despues de aca pasar a la pagina de carrito y checkout y agregar mas verificaciones


                } catch (Exception e) {
                        logger.error("Error al abrir la pagina principal: " + e.getMessage());
                        logger.error("Estado del test: Falló al intentar realizar la búsqueda de productos.");
                } finally {
                        driver.quit();
                }

                softAssert.assertAll();


        }
}