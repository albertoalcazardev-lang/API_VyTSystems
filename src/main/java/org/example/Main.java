package org.example;

import org.example.vista.Menu;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Menu.menu();
        } catch (IOException err) {
            System.out.println("Error al leer datos desde consola: " + err.getMessage());
        }
    }
}
