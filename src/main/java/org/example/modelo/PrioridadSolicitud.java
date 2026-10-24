package org.example.modelo;

public enum PrioridadSolicitud {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    private final String etiqueta;

    PrioridadSolicitud(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static PrioridadSolicitud desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        }

        String valor = texto.trim().toUpperCase();
        for (PrioridadSolicitud prioridad : values()) {
            if (prioridad.name().equals(valor) || prioridad.etiqueta.equalsIgnoreCase(texto.trim())) {
                return prioridad;
            }
        }

        throw new IllegalArgumentException("Prioridad invalida. Usa Alta, Media o Baja.");
    }
}
