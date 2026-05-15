package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.*;
import com.example.sportmanagerpro.services.AsistenciaService;
import com.example.sportmanagerpro.services.DeportistaService;
import com.example.sportmanagerpro.services.EntrenamientoService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AsistenciaController {

    @FXML
    private ComboBox<Entrenamiento> cmbEntrenamiento;

    @FXML
    private TableView<AsistenciaRow> tablaAsistencia;

    @FXML
    private TableColumn<AsistenciaRow, String> colNombre;

    @FXML
    private TableColumn<AsistenciaRow, String> colEstado;

    private final EntrenamientoService entrenamientoService = new EntrenamientoService();
    private final DeportistaService deportistaService = new DeportistaService();
    private final AsistenciaService asistenciaService = new AsistenciaService();

    @FXML
    public void initialize() {

        configurarTabla();
        cargarEntrenamientos();
    }

    private void configurarTabla() {

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getDeportista().getNombreCompleto()
                )
        );

        colEstado.setCellValueFactory(data ->
                data.getValue().estadoProperty()
        );

        colEstado.setCellFactory(columna -> new TableCell<>() {

            private final ComboBox<String> combo = new ComboBox<>(
                    FXCollections.observableArrayList(
                            "ASISTIO",
                            "FALTO",
                            "RETARDO",
                            "JUSTIFICADA",
                            "INJUSTIFICADA"
                    )
            );

            {
                combo.valueProperty().addListener((obs, anterior, nuevo) -> {

                    AsistenciaRow row = getTableView().getItems().get(getIndex());

                    row.setEstado(nuevo);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                combo.setValue(item);
                setGraphic(combo);
            }
        });
    }

    private void cargarEntrenamientos() {

        cmbEntrenamiento.setItems(
                FXCollections.observableArrayList(
                        entrenamientoService.listarTodos()
                )
        );
    }

    @FXML
    private void cargarDeportistasDelEntrenamiento() {

        Entrenamiento entrenamiento = cmbEntrenamiento.getValue();

        if (entrenamiento == null) {
            return;
        }

        tablaAsistencia.getItems().clear();

        var deportistas = deportistaService.listarPorCategoria(
                entrenamiento.getIdCategoria()
        );

        for (Deportista deportista : deportistas) {

            tablaAsistencia.getItems().add(
                    new AsistenciaRow(deportista, "ASISTIO")
            );
        }
    }

    @FXML
    private void guardarAsistencia() {

        Entrenamiento entrenamiento = cmbEntrenamiento.getValue();

        if (entrenamiento == null) {
            AlertUtils.mostrarError("Debe seleccionar un entrenamiento.");
            return;
        }

        try {

            for (AsistenciaRow row : tablaAsistencia.getItems()) {

                Asistencia asistencia = new Asistencia();

                asistencia.setIdEntrenamiento(
                        entrenamiento.getIdEntrenamiento()
                );

                asistencia.setIdDeportista(
                        row.getDeportista().getIdDeportista()
                );

                asistencia.setEstado(
                        row.getEstado()
                );

                asistenciaService.guardar(asistencia);
            }

            AlertUtils.mostrarInformacion(
                    "Asistencia guardada correctamente."
            );

        } catch (Exception e) {

            AlertUtils.mostrarError(e.getMessage());
        }
    }
}