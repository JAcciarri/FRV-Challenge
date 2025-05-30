package fravega.pojo;

public enum CuotasDisponibles {
     C_3("3"),
     C_6("6"),
     C_9("9"),
     C_12("12");

     private final String s;

     CuotasDisponibles(String s) {
          this.s = s;
     }

     public String asString() {
          return s;
     }
}