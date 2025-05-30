package fravega.dataproviders;

import org.testng.annotations.DataProvider;
import fravega.helpers.pojo.CuotasDisponibles;

public class CuotasDataProvider {

    @DataProvider(name = "injectAllCuotasAvailables")
    public static Object[][] cuotasProvider() {
        return new Object[][] {
                { CuotasDisponibles.C_3 },
                { CuotasDisponibles.C_6 },
                { CuotasDisponibles.C_9 },
                { CuotasDisponibles.C_12 }
        };
    }
}