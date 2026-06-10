package com.example.sportmanagerpro.nutricion.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Aplicación de prueba para ejecutar la interfaz de pacientes. */
public class SportNutriPatientsApp extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new PatientsModuleView(), 1500, 900);
        stage.setTitle("SportNutri Pro - Pacientes");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
