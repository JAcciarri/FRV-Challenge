package fravega.pojo;

import java.util.Arrays;

public enum TarjetaDeCredito {

        VISA("https://images.fravega.com/f300/d91d7904a85783a86377e30feb87e7ff.png.webp"),
        MASTERCARD("https://images.fravega.com/f300/54c0d769ece1b00f739360d6c900e4f9.png.webp"),
        AMEX("https://images.fravega.com/f300/4af8f5c4d9776dcbd6862adf577b3856.png.webp"),
        NARANJA("https://images.fravega.com/f300/5e3b1f271aff1e0b6f8f9ce4bf98beb7.png.webp"),
        CABAL("https://images.fravega.com/f300/11509c3fdfabb96a8fe2c3692726597b.png.webp");

        private final String src;
        TarjetaDeCredito(String imgSrc) {
            this.src = imgSrc;
        }

        public String getSrc() {
            return src;
        }

        @Override
        public String toString() {
            return this.name();
        }

        /**
         * Busca una tarjeta de crédito por su atributo src.
         *
         * @param src el atributo src de la tarjeta de crédito
         * @return la tarjeta de crédito correspondiente o null si no se encuentra
         */
        public static TarjetaDeCredito fromSrc(String src) {
                return Arrays.stream(TarjetaDeCredito.values())
                        .filter(c -> c.getSrc().equals(src))
                        .findFirst()
                        .orElse(null);
        }
}
