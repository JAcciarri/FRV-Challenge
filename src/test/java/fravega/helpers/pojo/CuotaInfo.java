package fravega.helpers.pojo;

public class CuotaInfo {
    private final String cuotas;
    private final String totalFinanced;
    private final String interest;

    public CuotaInfo(String cuotas, String totalFinanced, String interest) {
        this.cuotas = cuotas;
        this.totalFinanced = totalFinanced;
        this.interest = interest;
    }

    public String getCuotas() {
        return cuotas;
    }

    public String getTotalFinanced() {
        return totalFinanced;
    }

    public String getInterest() {
        return interest;
    }

    public boolean hasNoInterests() {
        return interest.trim().equalsIgnoreCase("$0");
    }
}