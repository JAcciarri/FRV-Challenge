package fravega.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class JsonTestDataLoader {

    public static <T> Object[][] loadGenericTestData(String filePath, TypeReference<List<T>> typeReference) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inpstr = JsonTestDataLoader.class.getClassLoader().getResourceAsStream(filePath);
        if (inpstr == null) {
            throw new IllegalArgumentException("No se encontró el archivo: " + filePath);
        }
        List<T> dataList = mapper.readValue(inpstr, typeReference);

        return dataList.stream()
                .map(data -> new Object[] { data })
                .toArray(Object[][]::new);
    }
}
