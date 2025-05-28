package tests;

import actions.CommonActions;
import base.ApplicationBaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pages.FravegaMainPage;
import pages.ProductPage;
import pages.ProductsSearchedPage;

import java.util.List;

public class CasoDeUsoHeladeraTest extends ApplicationBaseTest {

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
                                System.out.println("No se encontraron productos para la búsqueda.");
                                return; // Salimos del test si no hay productos
                        }

                        System.out.println("Cantidad de productos encontrados: " + products.size());

                        String originalPrice = productsSearchedPage.getProductOriginalPriceByIndex(1);
                        String discountedPrice = productsSearchedPage.getProductDiscountedPriceByIndex(1);
                        String productTitle = productsSearchedPage.getProductTitleByIndex(1);
                        System.out.println("Titulo del segundo producto: " + productTitle);
                        System.out.println("Precio original del segundo producto: " + originalPrice);
                        System.out.println("Precio con descuento del segundo producto: " + discountedPrice);

                        // TODO : IMPLEMENTAR UN LOGGER, NO USAR SYSTEM.OUT.PRINTLN

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
                        System.out.println("Error al abrir la pagina principal: " + e.getMessage());
                } finally {
                        driver.quit();
                }



        }
}
