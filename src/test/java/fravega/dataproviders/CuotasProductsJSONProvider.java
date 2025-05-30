package fravega.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import org.testng.annotations.DataProvider;
import java.util.List;
import java.util.Map;

public class CuotasProductsJSONProvider {

    @DataProvider(name = "cuotasNotebookDataProvider")
    public Object[][] cuotasNotebookDataProvider() throws Exception {
        return JsonTestDataLoader.loadGenericTestData(
                "data/cuotas_notebooks.json",
                new TypeReference<List<Map<String, String>>>() {}
        );
    }

    @DataProvider(name = "cuotasCelularesDataProvider")
    public Object[][] cuotasCelularesDataProvider() throws Exception {
        return JsonTestDataLoader.loadGenericTestData(
                "data/cuotas_celulares.json",
                new TypeReference<List<Map<String, String>>>() {}
        );
    }


}
