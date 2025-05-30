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

    @DataProvider(name = "cuotasHeladerasDataProvider")
    public Object[][] cuotasHeladerasDataProvider() throws Exception {
        return JsonTestDataLoader.loadGenericTestData(
                "data/cuotas_heladeras.json",
                new TypeReference<List<Map<String, String>>>() {}
        );
    }


}
