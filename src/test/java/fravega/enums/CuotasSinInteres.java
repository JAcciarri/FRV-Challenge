package fravega.enums;

public enum CuotasSinInteres {
    CUOTAS_3("https://images.fravega.com/f64/63132274c7db5975d86b4de2567a44da.png"),
    CUOTAS_6("https://images.fravega.com/f64/fbf39ae6c6394fb1c1f170cc05ccc38a.png"),
    CUOTAS_9("https://images.fravega.com/f64/42d4b4a62ceb4834620c1aceea65c19a.png"),
    CUOTAS_12("https://images.fravega.com/f64/a7ba469ee8ab23a770b519e31a1ed0e4.png");

    private final String src;
    CuotasSinInteres(String imgSrc) {
        this.src = imgSrc;
    }

    public String getSrc() {
        return src;
    }
}
