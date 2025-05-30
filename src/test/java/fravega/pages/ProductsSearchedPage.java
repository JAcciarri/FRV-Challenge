package fravega.pages;

import fravega.actions.CommonActions;
import fravega.pojo.CuotasDisponibles;
import fravega.pojo.CuotasSinInteresImg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import fravega.utils.LoggerUtil;

import java.util.List;
import java.util.Optional;

public class ProductsSearchedPage {

    CommonActions commonActions;
    private static final Logger logger = LoggerUtil.getLogger(ProductsSearchedPage.class);

    public ProductsSearchedPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }


    @FindBy(css = "[data-test-id='results-list']")
    public WebElement productsListContainer;
    @FindBy(xpath = "//*[@data-test-id='results-list']//*[@data-test-id='result-item']")
    public List<WebElement> productsList;

    public List<WebElement> getProductsList() {
        commonActions.waitForElementDisplayed(productsListContainer);
        return productsList;
    }

    public void selectProduct(WebElement product) {
        try {
            WebElement clickableProduct = product.findElement(By.xpath(".//a"));
            commonActions.clickElement(clickableProduct, "Link del producto");
            commonActions.waitForPageLoad();
            logger.info("Producto seleccionado correctamente.");
        } catch (Exception e) {
            logger.error("Error al seleccionar el producto: {}", e.getMessage(), e);
        }
    }

    public void selectFirstProduct() {
        try {
            if (productsList != null && !productsList.isEmpty()) {
                selectProduct(productsList.get(0));
            } else {
                // Probar encontrar de nuevo al lista y clickear el primer producto
                productsList = commonActions.findElements("//*[@data-test-id='results-list']//*[@data-test-id='result-item']");
                if (productsList != null && !productsList.isEmpty()) {
                    selectProduct(productsList.get(0));
                } else {
                    logger.warn("No se encontraron productos en la lista.");
                }
            }
        } catch (Exception e) {
            logger.error("Fallo al intentar seleccionar el primer producto: {}", e.getMessage(), e);
        }
    }

    /*
        * Busca entre los elementos que fueron resultado de la busqueda y luego
        * Selecciona el primer producto que tenga la cantidad de cuotas deseadas, matcheando por la imagen de cuotas.
     */
    public void selectFirstProductWithDesiredCuotas(CuotasDisponibles cuotas) {
        commonActions.waitForElementDisplayed(productsListContainer);
        CuotasSinInteresImg cuotaImg = CuotasSinInteresImg.fromCuotasDisponibles(cuotas);
        Optional<WebElement> producto = productsList.stream()
                .filter(p -> !p.findElements(By.xpath(".//img[contains(@src, '" + cuotaImg.getSrc() + "')]")).isEmpty())
                .findFirst();

        if (producto.isPresent()) {
            WebElement clickable = producto.get().findElement(By.xpath(".//a"));
            commonActions.clickElement(clickable, "Producto con " + cuotaImg.toString());
            commonActions.waitForPageLoad();
        } else {
            logger.warn("No se encontró ningún producto con {}", cuotaImg.toString());
        }
    }

    public String getProductOriginalPriceByIndex(int index) {
        WebElement product = productsList.get(index);
        WebElement priceElement = product.findElements(By.xpath(".//span[contains(text(), '$')]")).get(0);
        return commonActions.getTextFromElement(priceElement);
    }

    public String getProductDiscountedPriceByIndex(int index) {
        WebElement product = productsList.get(index);
        WebElement priceElement = product.findElements(By.xpath(".//span[contains(text(), '$')]")).get(1);
        return commonActions.getTextFromElement(priceElement);
    }

    public String getProductTitleByIndex(int index) {
        WebElement product = productsList.get(index);
        WebElement titleElement = product.findElement(By.xpath(".//span"));
        return commonActions.getTextFromElement(titleElement);
    }

    public void clickOnCuotasToFilterProducts(CuotasDisponibles cuotas){
        try{
            commonActions.waitForElementDisplayed(productsListContainer);
            String dynamicLocator = "//*[@name='installmentAggregation']//*[contains(text(),'@CUOTAS@')]".replace("@CUOTAS@", cuotas.asString());
            WebElement cuotasFilter = commonActions.findElement(dynamicLocator);
            commonActions.clickElement(cuotasFilter, "Filtro de cuotas: " + cuotas.asString());
        } catch (Exception e) {
            logger.warn("Error al hacer click en el filtro de cuotas: {}", e.getMessage(), e);
        }

    }


}
