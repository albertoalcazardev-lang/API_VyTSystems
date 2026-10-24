package org.example.modelo;

public enum EstadoSolicitud {
    ENTREGADA("Entregada"),
    EN_PROCESO("En proceso");

    private final String etiqueta;

    EstadoSolicitud(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static EstadoSolicitud desdeTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la solicitud es obligatorio.");
        }

        String valor = texto.trim().replace(" ", "_").toUpperCase();
        for (EstadoSolicitud estado : values()) {
            if (estado.name().equals(valor) || estado.etiqueta.equalsIgnoreCase(texto.trim())) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado invalido. Usa Entregada o En proceso.");
    }
}
