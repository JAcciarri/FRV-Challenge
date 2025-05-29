package utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


public class LoggerUtil {

    // Clase utility para obtener instancias de Logger
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz.getName());
    }

    public static void printAPIListResponse(List<Map<String, Object>> mapList, Logger logger) {
        if (logger.isInfoEnabled()) {
            for(Map<String, Object> entity : mapList){
                logger.info("__________________________");
                entity.forEach((key, value) -> logger.info("{}: {}", key, value));
            }
        }
    }

}
