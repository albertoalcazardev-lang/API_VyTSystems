package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = obtenerVariable("DB_URL", "jdbc:mysql://127.0.0.1:3306/auditoriaciudadana");
    private static final String USUARIO = obtenerVariable("DB_USER", "root");
    private static final String PASSWORD = obtenerVariable("DB_PASSWORD", "");

    private Conexion() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    private static String obtenerVariable(String nombre, String valorPredeterminado) {
        String valor = System.getenv(nombre);
        return valor == null || valor.isBlank() ? valorPredeterminado : valor;
    }
}
