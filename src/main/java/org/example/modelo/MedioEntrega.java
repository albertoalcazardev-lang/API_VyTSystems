package org.example.modelo;

public enum MedioEntrega {
    DIGITAL("Digital"),
    IMPRESO("Impreso");

    private final String etiqueta;

    MedioEntrega(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static MedioEntrega desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El medio de entrega es obligatorio.");
        }

        String valor = texto.trim().replace(" ", "_").toUpperCase();
        for (MedioEntrega medio : values()) {
            if (medio.name().equals(valor) || medio.etiqueta.equalsIgnoreCase(texto.trim())) {
                return medio;
            }
        }

        throw new IllegalArgumentException("Medio invalido. Usa Digital o Impreso.");
    }
}
