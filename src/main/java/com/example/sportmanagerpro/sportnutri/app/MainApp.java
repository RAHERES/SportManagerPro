package com.example.sportmanagerpro.sportnutri.app;


import com.example.sportmanagerpro.sportnutri.ui.AppShell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        AppShell root = new AppShell();

        Scene scene = new Scene(root, 1500, 900);
        scene.getStylesheets().add(
                com.example.sportmanagerpro.MainApp.class.getResource("dashboard.css").toExternalForm()
        );

        stage.setTitle("SportNutri Manager Pro");
        stage.setMinWidth(1200);
        stage.setMinHeight(760);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}