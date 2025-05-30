package fravega.helpers;

import fravega.helpers.pojo.CuotaInfo;
import fravega.helpers.pojo.CuotasDisponibles;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



public class CuotaHelper {

    public static Optional<CuotaInfo> filterCuotasByQuantity(List<CuotaInfo> cuotas, CuotasDisponibles quantity) {
        return cuotas.stream()
                .filter(c -> c.getCuotas().contains(quantity.asString()))
                .findFirst();
    }

    public static List<CuotaInfo> filterCuotasWithInterests(List<CuotaInfo> cuotas) {
        return cuotas.stream()
                .filter(c -> !c.hasNoInterests())
                .collect(Collectors.toList());
    }

    public static List<CuotaInfo> flattenCuotasForAllBanks(Map<String, List<CuotaInfo>> allCuotasAllBanks){
            return allCuotasAllBanks.values().stream()
                        .flatMap(List::stream)
                        .toList();
    }

    public static String getURLCuota(CuotasDisponibles quantity) {
        String URL = "https://www.fravega.com/l/?formas-de-pago=@REPLACE@-cuotas-sin-interes";
        return URL.replace("@REPLACE@", quantity.asString());
    }
}
