package com.example.sportmanagerpro.sportnutri1.app;

import com.example.sportmanagerpro.sportnutri1.ui.RegistroPacientesModernView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada del módulo de registro moderno de pacientes SportNutri.
 */
public class RegistroPacientesModernApp extends Application {
    @Override
    public void start(Stage stage) {
        RegistroPacientesModernView view = new RegistroPacientesModernView();
        stage.setTitle("SportNutri Pro - Registro moderno de pacientes NOM-004");
        stage.setScene(view.crearEscena());
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
