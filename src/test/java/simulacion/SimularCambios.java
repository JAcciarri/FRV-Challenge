package simulacion;

public class SimularCambios {
    /*
    Clase de simulacion de cambios para la branch feature-cuotas.
    Cualquier PR que se mergee a esta branch va a disparar el workflow configurado en Jenkins
     */

    String mensaje = "Simulando cambios simulados en la branch feature-cuotas para pruebas de integración continua.";
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
