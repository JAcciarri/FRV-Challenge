package tests;

import actions.CommonActions;
import base.ApplicationBaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class PrimerCasoDeUsoTest extends ApplicationBaseTest {

        @Test
        public void verificarCasoDeUsoHeladera(){
                WebDriver driver = getDriver();
                CommonActions commonActions = new CommonActions(driver);
        }
}
