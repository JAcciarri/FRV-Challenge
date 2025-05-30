package fravega.helpers;

/**
 * Centraliza los selectores dinámicos usados en el modal de cuotas.
 * Permite mantener consistencia y facilita el mantenimiento ante cambios en el HTML.
 */
public class CuotaConstants {

    // Localiza cada banco visible dentro del dropdown de bancos
    public static final String DYNAMIC_ALL_BANKS_FOR_A_CREDIT_CARD = "//div[@class='detail']";

    // Elemento que despliega el selector de bancos
    public static final String DYNAMIC_SELECT_BANK = "//h3[text()='Banco']";

    // Cada fila de cuotas dentro de la tabla de pagos disponibles
    public static final String DYNAMIC_ALL_PAYMENTS = "//tr[@class='MuiTableRow-root']";

    // Selectores RELATIVOS utilizados dentro de cada fila (tr)
    // Estos deben usarse sobre elementos individuales tipo `payment.findElement(...)`
    public static final String ADD_REL_XPATH_ROW_CUOTAS = ".//td[1]";             // Cantidad de cuotas (ej: "3 cuotas")
    public static final String ADD_REL_XPATH_ROW_TOTAL_FINANCED = ".//td[3]";     // Precio total financiado
    public static final String ADD_REL_XPATH_ROW_INTEREST = ".//td[4]";           // Monto de intereses

    // Selectores dinamicos relativos a las promociones bancarias - cantidad de cuotas que ofrecen sin interes
    public  static final String DYNAMIC_PROMOTIONS_WITHOUT_INTEREST_6 = "//div[@data-test-id='payment-tooltip']//b[text()='6']";
    public static final String ADD_REL_XPATH_DYNAMIC_CARDS_TO_BE_SELECTED = ".//..//..//..//img";

}

