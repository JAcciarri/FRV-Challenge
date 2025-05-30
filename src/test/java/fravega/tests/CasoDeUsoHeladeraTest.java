package fravega.tests;

import fravega.actions.CommonActions;
import fravega.base.ApplicationBaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import fravega.pages.CheckoutPage;
import fravega.pages.FravegaMainPage;
import fravega.pages.ProductPage;
import fravega.pages.ProductsSearchedPage;
import fravega.utils.LoggerUtil;

import java.util.List;

public class CasoDeUsoHeladeraTest extends ApplicationBaseTest {

        private static final Logger logger = LoggerUtil.getLogger(CasoDeUsoHeladeraTest.class);

        @Parameters({"inputDataSearch"})
        @Test
        public void verificarCasoDeUsoHeladera(String inputDataSearch) {
                WebDriver driver = getDriver();
                CommonActions commonActions = new CommonActions(driver);
                FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);
                ProductsSearchedPage productsSearchedPage = new ProductsSearchedPage(driver);
                SoftAssert softAssert = new SoftAssert();
                ProductPage productPage = new ProductPage(driver);
                CheckoutPage checkoutPage = new CheckoutPage(driver);

                logger.info("Iniciando el test de caso de uso heladera");

                try {
                        fravegaMainPage.openMainPageAndHandleModal();
                        softAssert.assertTrue(commonActions.isElementDisplayed(fravegaMainPage.inputSearchProduct),
                                "El campo de búsqueda no está visible en la página principal.");

                        fravegaMainPage.searchProduct(inputDataSearch);
                        commonActions.waitForPageLoad();
                        List<WebElement> products = productsSearchedPage.getProductsList();
                        final int INDEX_DESIRED_PRODUCT = 1; // Segundo producto de la lista

                        /* Hacemos un hard assert para asegurarnos de que la lista de productos no esté vacía,
                        * ya que si lo está, no podemos continuar con el test.
                        */
                        Assert.assertFalse(products.isEmpty(), "No se encontraron productos para esa búsqueda");

                        logger.info("Cantidad de productos encontrados: " + products.size());

                        String originalPrice = productsSearchedPage.getProductOriginalPriceByIndex(INDEX_DESIRED_PRODUCT);
                        String discountedPrice = productsSearchedPage.getProductDiscountedPriceByIndex(INDEX_DESIRED_PRODUCT);
                        String productTitle = productsSearchedPage.getProductTitleByIndex(INDEX_DESIRED_PRODUCT);
                        logger.info("Titulo del segundo producto: " + productTitle);
                        logger.info("Precio original del segundo producto: " + originalPrice);
                        logger.info("Precio con descuento del segundo producto: " + discountedPrice);

                        // Ahora vamos a hacer click en el segundo producto de la lista y verificar
                        productsSearchedPage.selectProduct(products.get(INDEX_DESIRED_PRODUCT));

                        softAssert.assertEquals(productPage.getProductTitle(), productTitle,
                                "El título del producto no coincide con el esperado.");
                        softAssert.assertEquals(productPage.getProductDiscountedPrice(), discountedPrice,
                                "El precio con descuento del producto no coincide con el esperado.");
                        softAssert.assertEquals(productPage.getProductOriginalPrice(), originalPrice,
                                "El precio original del producto no coincide con el esperado.");

                        // SE ASUME QUE SI EL BOTON DE COMPRAR ESTA VISIBLE Y ENABLED, EL PRODUCTO ESTA DISPONIBLE
                        if(productPage.isBuyButtonEnabled()) {
                                logger.info("El botón de comprar ahora está visible y habilitado.");
                        } else {
                                softAssert.fail("El botón de comprar no está visible o habilitado.");
                        }

                        productPage.buyProduct();
                        checkoutPage.waitForCheckoutPageToLoad();
                        final int EXPECTED_AMOUNT_OF_PRODUCTS = 1; // Verificar que solo se agrega un producto al checkout
                        softAssert.assertTrue(commonActions.isElementDisplayed(checkoutPage.checkoutTitle),
                                "El título de la página de checkout no está visible.");
                        softAssert.assertEquals(checkoutPage.getProductTitleInCheckout(), productTitle,
                                "El título del producto en la página de checkout no coincide con el esperado.");
                        softAssert.assertEquals(checkoutPage.getTotalPriceInCheckout(), discountedPrice,
                                "El precio del producto en la página de checkout no coincide con el esperado.");
                        softAssert.assertEquals(checkoutPage.getAmountOfProductsInCheckout(), String.valueOf(EXPECTED_AMOUNT_OF_PRODUCTS),
                                "La cantidad de productos en el checkout no coincide con la esperada.");


                } catch (Exception e) {
                        logger.error("Estado del test: Falló. {}", e.getMessage());
                } finally {
                     // Driver quit fue movido a @AfterMethod en ApplicationBaseTest
                }

                softAssert.assertAll();
        }
}