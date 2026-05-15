package com.example.sportmanagerpro.controllers;

/**
 * Controlador principal de la ventana base.
 */

import com.example.sportmanagerpro.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador principal de la aplicación.
 */
public class MainController {

    @FXML
    public void initialize() {
        mostrarDashboard();
    }

    @FXML
    private StackPane contentPane;

    @FXML
    private void mostrarCategorias() {
        cargarVista("categorias-view.fxml");
    }

    @FXML
    private void mostrarEntrenamientos() {
        cargarVista("entrenamientos-view.fxml");
    }

    @FXML
    private void mostrarAsistencia() {
        cargarVista("asistencias-view.fxml");
    }

    @FXML
    private void mostrarEvaluaciones() {
        cargarVista("evaluaciones-view.fxml");
    }

    @FXML
    private void mostrarCompetencias() {
        cargarVista("competencias-view.fxml");
    }

    @FXML
    private void mostrarPartidos() {
        cargarVista("partidos-view.fxml");
    }



    @FXML
    private void mostrarEstadisticasPartido() {
        cargarVista("estadisticas-partido-view.fxml");
    }


    @FXML
    private void mostrarHistorialDeportista() {
        cargarVista("historial-deportista-view.fxml");
    }

    @FXML
    private void mostrarLesiones() {
        cargarVista("lesiones-view.fxml");
    }

    @FXML
    private void mostrarObservaciones() {
        cargarVista("observaciones-view.fxml");
    }

    @FXML
    private void mostrarDashboard() {
        cargarVista("dashboard-view.fxml");
    }

    @FXML
    private void abrirModuloPlanificacion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    //MainApp.class.getResource("planificacion-view.fxml")
                    MainApp.class.getResource("distribucion-tiempos-view.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Planificación y Periodización");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarReporteCategoria() {
        cargarVista("reporte-categoria-view.fxml");
    }

    private void cargarVista(String rutaVista) {
        try {
            Node vista = FXMLLoader.load(MainApp.class.getResource(rutaVista));
            contentPane.getChildren().setAll(vista);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la vista: " + rutaVista, e);
        }
    }
    @FXML
    private void mostrarDeportistas() {
        cargarVista("deportistas-view.fxml");
    }
}