package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Deportista;
import com.example.sportmanagerpro.models.ObservacionDeportiva;
import com.example.sportmanagerpro.services.DeportistaService;
import com.example.sportmanagerpro.services.ObservacionDeportivaService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ObservacionDeportivaController {

    @FXML private ComboBox<Deportista> cmbDeportista;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextArea txtObservacion;

    @FXML private TableView<ObservacionDeportiva> tablaObservaciones;
    @FXML private TableColumn<ObservacionDeportiva, Integer> colId;
    @FXML private TableColumn<ObservacionDeportiva, String> colFecha;
    @FXML private TableColumn<ObservacionDeportiva, Integer> colDeportista;
    @FXML private TableColumn<ObservacionDeportiva, String> colTipo;
    @FXML private TableColumn<ObservacionDeportiva, String> colObservacion;

    private final DeportistaService deportistaService = new DeportistaService();
    private final ObservacionDeportivaService observacionService = new ObservacionDeportivaService();

    private ObservacionDeportiva observacionSeleccionada;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        cargarDeportistas();
        cargarObservaciones();

        tablaObservaciones.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) -> cargarObservacionSeleccionada(seleccionada)
        );
    }

    @FXML
    private void guardarObservacion() {
        try {
            ObservacionDeportiva observacion = leerFormulario();
            observacionService.guardar(observacion);
            AlertUtils.mostrarInformacion("Observación guardada correctamente.");
            limpiarFormulario();
            cargarObservaciones();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarObservacion() {
        if (observacionSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar una observación.");
            return;
        }

        try {
            ObservacionDeportiva observacion = leerFormulario();
            observacion.setIdObservacion(observacionSeleccionada.getIdObservacion());
            observacionService.actualizar(observacion);
            AlertUtils.mostrarInformacion("Observación actualizada correctamente.");
            limpiarFormulario();
            cargarObservaciones();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        observacionSeleccionada = null;
        cmbDeportista.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        cmbTipo.getSelectionModel().clearSelection();
        txtObservacion.clear();
        tablaObservaciones.getSelectionModel().clearSelection();
    }

    private void configurarCombos() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Técnica",
                "Táctica",
                "Física",
                "Actitudinal",
                "Disciplina",
                "Competitiva",
                "Mentalidad",
                "Compromiso",
                "Recomendación",
                "Otro"
        ));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdObservacion()).asObject());

        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFecha() != null ? data.getValue().getFecha().toString() : ""
                ));

        colDeportista.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdDeportista()).asObject());

        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipo()));

        colObservacion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getObservacion()));
    }

    private void cargarDeportistas() {
        cmbDeportista.setItems(
                FXCollections.observableArrayList(deportistaService.listarTodas())
        );

        cmbDeportista.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Deportista deportista, boolean empty) {
                super.updateItem(deportista, empty);
                setText(empty || deportista == null ? null : deportista.getNombreCompleto());
            }
        });

        cmbDeportista.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Deportista deportista, boolean empty) {
                super.updateItem(deportista, empty);
                setText(empty || deportista == null ? null : deportista.getNombreCompleto());
            }
        });
    }

    private void cargarObservaciones() {
        tablaObservaciones.setItems(
                FXCollections.observableArrayList(observacionService.listarTodas())
        );
    }

    private ObservacionDeportiva leerFormulario() {
        Deportista deportista = cmbDeportista.getValue();

        ObservacionDeportiva o = new ObservacionDeportiva();
        o.setIdDeportista(deportista != null ? deportista.getIdDeportista() : 0);
        o.setFecha(dpFecha.getValue());
        o.setTipo(cmbTipo.getValue());
        o.setObservacion(txtObservacion.getText());

        return o;
    }

    private void cargarObservacionSeleccionada(ObservacionDeportiva o) {
        if (o == null) {
            return;
        }

        observacionSeleccionada = o;

        dpFecha.setValue(o.getFecha());
        cmbTipo.setValue(o.getTipo());
        txtObservacion.setText(o.getObservacion());

        for (Deportista deportista : cmbDeportista.getItems()) {
            if (deportista.getIdDeportista() == o.getIdDeportista()) {
                cmbDeportista.setValue(deportista);
                break;
            }
        }
    }
}