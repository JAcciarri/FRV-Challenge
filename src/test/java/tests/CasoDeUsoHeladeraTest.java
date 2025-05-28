package tests;

import actions.CommonActions;
import base.ApplicationBaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import pages.FravegaMainPage;

public class CasoDeUsoHeladeraTest extends ApplicationBaseTest {

        @Test
        public void verificarCasoDeUsoHeladera(){
                WebDriver driver = getDriver();
                CommonActions commonActions = new CommonActions(driver);
                FravegaMainPage fravegaMainPage = new FravegaMainPage(driver);

                try {
                        fravegaMainPage.openMainPageAndHandleModal();
                        // ahora esta hardcodeado, pero los input se van a tomar del archivo json
                        fravegaMainPage.searchProduct("heladera");
                        commonActions.waitForPageLoad();

                } catch (Exception e) {
                        System.out.println("Error al abrir la pagina principal: " + e.getMessage());
                } finally {
                        driver.quit();
                }



        }
}
