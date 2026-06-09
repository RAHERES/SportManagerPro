package com.example.sportmanagerpro.sportnutri.app;

import com.example.sportmanagerpro.sportnutri.ui.ConsultaExpedientesModernView;
import com.example.sportmanagerpro.sportnutri.ui.NuevoExpedienteView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        //NuevoExpedienteView view = new NuevoExpedienteView();
        ConsultaExpedientesModernView view = new ConsultaExpedientesModernView();

        Scene scene = new Scene(view, 1500, 900);

        URL css = getClass().getResource("/styles/nutrisport.css");

        URL resource = com.example.sportmanagerpro.MainApp.class.getResource("verinfo.css");
        if (resource != null) {
            scene.getStylesheets().add(resource.toExternalForm());
        } else {
            System.out.println("No se encontró /styles/verinfo.css");
        }

        stage.setTitle("NutriSport Pro - Nuevo expediente");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}