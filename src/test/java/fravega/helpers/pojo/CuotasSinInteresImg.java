package fravega.helpers.pojo;

public enum CuotasSinInteresImg {
    CUOTAS_3("https://images.fravega.com/f64/63132274c7db5975d86b4de2567a44da.png", "3 cuotas"),
    CUOTAS_6("https://images.fravega.com/f64/fbf39ae6c6394fb1c1f170cc05ccc38a.png", "6 cuotas"),
    CUOTAS_9("https://images.fravega.com/f64/42d4b4a62ceb4834620c1aceea65c19a.png", "9 cuotas"),
    CUOTAS_12("https://images.fravega.com/f64/a7ba469ee8ab23a770b519e31a1ed0e4.png", "12 cuotas");

    private final String src;
    private final String cuotas;
    CuotasSinInteresImg(String imgSrc, String cuotas) {
        this.src = imgSrc;
        this.cuotas = cuotas;
    }

    public String getSrc() {
        return src;
    }
    @Override
    public String toString() {
        return cuotas;
    }
}
