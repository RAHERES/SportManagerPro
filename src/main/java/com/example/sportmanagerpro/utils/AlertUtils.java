package com.example.sportmanagerpro.utils;

import javafx.scene.control.Alert;

/**
 * Utilidad para mostrar mensajes al usuario.
 */
public class AlertUtils {

    private AlertUtils() {
    }

    public static void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}