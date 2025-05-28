package utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerUtil {
    // Clase utility para obtener instancias de Logger
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz.getName());
    }
}
