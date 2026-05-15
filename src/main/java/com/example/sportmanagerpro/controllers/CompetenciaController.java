package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Competencia;
import com.example.sportmanagerpro.services.CompetenciaService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CompetenciaController {

    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtSede;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private TextArea txtDescripcion;
    @FXML private TextArea txtObservaciones;

    @FXML private TableView<Competencia> tablaCompetencias;
    @FXML private TableColumn<Competencia, Integer> colId;
    @FXML private TableColumn<Competencia, String> colNombre;
    @FXML private TableColumn<Competencia, String> colTipo;
    @FXML private TableColumn<Competencia, String> colSede;
    @FXML private TableColumn<Competencia, String> colFechaInicio;
    @FXML private TableColumn<Competencia, String> colFechaFin;

    private final CompetenciaService competenciaService = new CompetenciaService();

    private Competencia competenciaSeleccionada;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        cargarCompetencias();

        tablaCompetencias.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) -> cargarCompetenciaSeleccionada(seleccionada)
        );
    }

    @FXML
    private void guardarCompetencia() {
        try {
            Competencia competencia = leerFormulario();
            competenciaService.guardar(competencia);
            AlertUtils.mostrarInformacion("Competencia guardada correctamente.");
            limpiarFormulario();
            cargarCompetencias();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarCompetencia() {
        if (competenciaSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar una competencia.");
            return;
        }

        try {
            Competencia competencia = leerFormulario();
            competencia.setIdCompetencia(competenciaSeleccionada.getIdCompetencia());
            competenciaService.actualizar(competencia);
            AlertUtils.mostrarInformacion("Competencia actualizada correctamente.");
            limpiarFormulario();
            cargarCompetencias();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        competenciaSeleccionada = null;
        txtNombre.clear();
        cmbTipo.getSelectionModel().clearSelection();
        txtSede.clear();
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
        txtDescripcion.clear();
        txtObservaciones.clear();
        tablaCompetencias.getSelectionModel().clearSelection();
    }

    private void configurarCombos() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Torneo",
                "Liga",
                "Eliminatoria",
                "Amistoso",
                "Final",
                "Intercolegial",
                "Otro"
        ));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdCompetencia()).asObject());

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombre()));

        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipo()));

        colSede.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSede()));

        colFechaInicio.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFechaInicio() != null
                                ? data.getValue().getFechaInicio().toString()
                                : ""
                ));

        colFechaFin.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFechaFin() != null
                                ? data.getValue().getFechaFin().toString()
                                : ""
                ));
    }

    private void cargarCompetencias() {
        tablaCompetencias.setItems(
                FXCollections.observableArrayList(competenciaService.listarTodas())
        );
    }

    private Competencia leerFormulario() {
        Competencia c = new Competencia();
        c.setNombre(txtNombre.getText());
        c.setTipo(cmbTipo.getValue());
        c.setSede(txtSede.getText());
        c.setFechaInicio(dpFechaInicio.getValue());
        c.setFechaFin(dpFechaFin.getValue());
        c.setDescripcion(txtDescripcion.getText());
        c.setObservaciones(txtObservaciones.getText());
        return c;
    }

    private void cargarCompetenciaSeleccionada(Competencia c) {
        if (c == null) {
            return;
        }

        competenciaSeleccionada = c;

        txtNombre.setText(c.getNombre());
        cmbTipo.setValue(c.getTipo());
        txtSede.setText(c.getSede());
        dpFechaInicio.setValue(c.getFechaInicio());
        dpFechaFin.setValue(c.getFechaFin());
        txtDescripcion.setText(c.getDescripcion());
        txtObservaciones.setText(c.getObservaciones());
    }
}