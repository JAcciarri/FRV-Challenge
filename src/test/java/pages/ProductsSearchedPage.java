package pages;

import actions.CommonActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProductsSearchedPage {

    CommonActions commonActions;

    public ProductsSearchedPage(WebDriver driver) {
        this.commonActions = new CommonActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[data-test-id='results-list']")
    public WebElement productsListContainer;
    @FindBy(css = "[data-test-id='results-list'] [data-test-id='result-item']")
    public List<WebElement> productsList;

    public List<WebElement> getProductsList() {
        commonActions.waitForElementDisplayed(productsListContainer);
        return productsList;
    }

    public void selectProduct(WebElement product) {
        WebElement clickableProduct = product.findElement(By.xpath(".//a"));
        commonActions.clickElement(clickableProduct);
    }

}
