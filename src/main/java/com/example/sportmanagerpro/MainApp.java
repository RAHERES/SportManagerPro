package com.example.sportmanagerpro;


import com.example.sportmanagerpro.database.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación SportManager Pro.
 * Inicializa la base de datos y carga la ventana principal.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initializeDatabase();

        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("hello-view" +
                        ".fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 720);
        scene.getStylesheets().add(
                MainApp.class.getResource("app.css").toExternalForm()
        );
        stage.setTitle("SportManager Pro");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}