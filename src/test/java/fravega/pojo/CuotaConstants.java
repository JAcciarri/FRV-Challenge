package fravega.pojo;

public class CuotaConstants {

    /* Clase creada para definir las constantes de los locators de las cuotas, y en caso de ser modificados en el codigo fuente alguna vez
    simplemente pueden ser modificados aca y no en cada test o pagina que los utilice.
     */

    public static final String DYNAMIC_ALL_BANKS_FOR_A_CREDIT_CARD = "//div[@class='detail']";
    public static final String DYNAMIC_SELECT_BANK = "//h3[text()='Banco']";
    public static final String DYNAMIC_ALL_PAYMENTS = "//tr[@class='MuiTableRow-root']";
    public static final String ADD_XPATH_ROW_CUOTAS = ".//td[1]";
    public static final String ADD_XPATH_ROW_TOTAL_FINANCED = ".//td[3]";
    public static final String ADD_XPATH_ROW_INTEREST = ".//td[4]";


}
