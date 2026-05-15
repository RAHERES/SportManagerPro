package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Deportista;
import com.example.sportmanagerpro.models.EvaluacionFisica;
import com.example.sportmanagerpro.services.DeportistaService;
import com.example.sportmanagerpro.services.EvaluacionFisicaService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EvaluacionFisicaController {

    @FXML private ComboBox<Deportista> cmbDeportista;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbPrueba;
    @FXML private TextField txtResultado;
    @FXML private TextField txtUnidad;
    @FXML private TextField txtEvaluador;
    @FXML private TextArea txtObservaciones;

    @FXML private TableView<EvaluacionFisica> tablaEvaluaciones;
    @FXML private TableColumn<EvaluacionFisica, Integer> colId;
    @FXML private TableColumn<EvaluacionFisica, String> colFecha;
    @FXML private TableColumn<EvaluacionFisica, Integer> colDeportista;
    @FXML private TableColumn<EvaluacionFisica, String> colPrueba;
    @FXML private TableColumn<EvaluacionFisica, Double> colResultado;
    @FXML private TableColumn<EvaluacionFisica, String> colUnidad;
    @FXML private TableColumn<EvaluacionFisica, String> colEvaluador;

    private final DeportistaService deportistaService = new DeportistaService();
    private final EvaluacionFisicaService evaluacionService = new EvaluacionFisicaService();

    private EvaluacionFisica evaluacionSeleccionada;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        cargarDeportistas();
        cargarEvaluaciones();

        tablaEvaluaciones.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) -> cargarEvaluacionSeleccionada(seleccionada)
        );
    }

    @FXML
    private void guardarEvaluacion() {
        try {
            EvaluacionFisica evaluacion = leerFormulario();
            evaluacionService.guardar(evaluacion);
            AlertUtils.mostrarInformacion("Evaluación física guardada correctamente.");
            limpiarFormulario();
            cargarEvaluaciones();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarEvaluacion() {
        if (evaluacionSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar una evaluación.");
            return;
        }

        try {
            EvaluacionFisica evaluacion = leerFormulario();
            evaluacion.setIdEvaluacion(evaluacionSeleccionada.getIdEvaluacion());
            evaluacionService.actualizar(evaluacion);
            AlertUtils.mostrarInformacion("Evaluación física actualizada correctamente.");
            limpiarFormulario();
            cargarEvaluaciones();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        evaluacionSeleccionada = null;
        cmbDeportista.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        cmbPrueba.getSelectionModel().clearSelection();
        txtResultado.clear();
        txtUnidad.clear();
        txtEvaluador.clear();
        txtObservaciones.clear();
        tablaEvaluaciones.getSelectionModel().clearSelection();
    }

    @FXML
    private void actualizarUnidadSugerida() {
        String prueba = cmbPrueba.getValue();

        if (prueba == null) {
            return;
        }

        switch (prueba) {
            case "Velocidad 10 m", "Velocidad 20 m", "Velocidad 30 m", "Agilidad", "Core" ->
                    txtUnidad.setText("segundos");

            case "Salto vertical", "Flexibilidad" ->
                    txtUnidad.setText("centímetros");

            case "Resistencia" ->
                    txtUnidad.setText("metros");

            case "Fuerza tren inferior", "Fuerza tren superior" ->
                    txtUnidad.setText("repeticiones");

            default ->
                    txtUnidad.clear();
        }
    }

    private void configurarCombos() {
        cmbPrueba.setItems(FXCollections.observableArrayList(
                "Velocidad 10 m",
                "Velocidad 20 m",
                "Velocidad 30 m",
                "Salto vertical",
                "Agilidad",
                "Resistencia",
                "Fuerza tren inferior",
                "Fuerza tren superior",
                "Core",
                "Flexibilidad"
        ));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdEvaluacion()).asObject());

        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFecha().toString()));

        colDeportista.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdDeportista()).asObject());

        colPrueba.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPrueba()));

        colResultado.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getResultado()).asObject());

        colUnidad.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUnidad()));

        colEvaluador.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEvaluador()));
    }

    private void cargarDeportistas() {
        cmbDeportista.setItems(FXCollections.observableArrayList(deportistaService.listarTodas()));

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

    private void cargarEvaluaciones() {
        tablaEvaluaciones.setItems(
                FXCollections.observableArrayList(evaluacionService.listarTodas())
        );
    }

    private EvaluacionFisica leerFormulario() {
        Deportista deportista = cmbDeportista.getValue();

        EvaluacionFisica e = new EvaluacionFisica();
        e.setIdDeportista(deportista != null ? deportista.getIdDeportista() : 0);
        e.setFecha(dpFecha.getValue());
        e.setPrueba(cmbPrueba.getValue());
        e.setResultado(parseDouble(txtResultado.getText()));
        e.setUnidad(txtUnidad.getText());
        e.setEvaluador(txtEvaluador.getText());
        e.setObservaciones(txtObservaciones.getText());

        return e;
    }

    private void cargarEvaluacionSeleccionada(EvaluacionFisica e) {
        if (e == null) {
            return;
        }

        evaluacionSeleccionada = e;

        dpFecha.setValue(e.getFecha());
        cmbPrueba.setValue(e.getPrueba());
        txtResultado.setText(String.valueOf(e.getResultado()));
        txtUnidad.setText(e.getUnidad());
        txtEvaluador.setText(e.getEvaluador());
        txtObservaciones.setText(e.getObservaciones());

        for (Deportista deportista : cmbDeportista.getItems()) {
            if (deportista.getIdDeportista() == e.getIdDeportista()) {
                cmbDeportista.setValue(deportista);
                break;
            }
        }
    }

    private double parseDouble(String texto) {
        try {
            return Double.parseDouble(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El resultado debe ser un número válido.");
        }
    }
}