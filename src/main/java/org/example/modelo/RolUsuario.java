package org.example.modelo;

public enum RolUsuario {
    ADMINISTRADOR("Administrador"),
    CONSULTA("Usuario de consulta");

    private final String etiqueta;

    RolUsuario(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
