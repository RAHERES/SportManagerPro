package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Deportista;
import com.example.sportmanagerpro.models.Lesion;
import com.example.sportmanagerpro.services.DeportistaService;
import com.example.sportmanagerpro.services.LesionService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LesionController {

    @FXML private ComboBox<Deportista> cmbDeportista;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbZonaAfectada;
    @FXML private ComboBox<String> cmbTipoMolestia;
    @FXML private ComboBox<String> cmbContexto;
    @FXML private TextField txtAccionTomada;
    @FXML private DatePicker dpFechaRegreso;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TextArea txtObservaciones;

    @FXML private TableView<Lesion> tablaLesiones;
    @FXML private TableColumn<Lesion, Integer> colId;
    @FXML private TableColumn<Lesion, String> colFecha;
    @FXML private TableColumn<Lesion, Integer> colDeportista;
    @FXML private TableColumn<Lesion, String> colZona;
    @FXML private TableColumn<Lesion, String> colTipo;
    @FXML private TableColumn<Lesion, String> colContexto;
    @FXML private TableColumn<Lesion, String> colEstado;
    @FXML private TableColumn<Lesion, String> colRegreso;

    private final DeportistaService deportistaService = new DeportistaService();
    private final LesionService lesionService = new LesionService();

    private Lesion lesionSeleccionada;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        cargarDeportistas();
        cargarLesiones();

        tablaLesiones.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) -> cargarLesionSeleccionada(seleccionada)
        );
    }

    @FXML
    private void guardarLesion() {
        try {
            Lesion lesion = leerFormulario();
            lesionService.guardar(lesion);
            AlertUtils.mostrarInformacion("Seguimiento físico guardado correctamente.");
            limpiarFormulario();
            cargarLesiones();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarLesion() {
        if (lesionSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar un registro.");
            return;
        }

        try {
            Lesion lesion = leerFormulario();
            lesion.setIdLesion(lesionSeleccionada.getIdLesion());
            lesionService.actualizar(lesion);
            AlertUtils.mostrarInformacion("Seguimiento físico actualizado correctamente.");
            limpiarFormulario();
            cargarLesiones();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        lesionSeleccionada = null;
        cmbDeportista.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        cmbZonaAfectada.getSelectionModel().clearSelection();
        cmbTipoMolestia.getSelectionModel().clearSelection();
        cmbContexto.getSelectionModel().clearSelection();
        txtAccionTomada.clear();
        dpFechaRegreso.setValue(null);
        cmbEstado.getSelectionModel().clearSelection();
        txtObservaciones.clear();
        tablaLesiones.getSelectionModel().clearSelection();
    }

    private void configurarCombos() {
        cmbZonaAfectada.setItems(FXCollections.observableArrayList(
                "Cabeza",
                "Cuello",
                "Hombro",
                "Brazo",
                "Codo",
                "Muñeca",
                "Espalda",
                "Cadera",
                "Muslo",
                "Rodilla",
                "Pantorrilla",
                "Tobillo",
                "Pie",
                "General"
        ));

        cmbTipoMolestia.setItems(FXCollections.observableArrayList(
                "Dolor",
                "Golpe",
                "Torcedura",
                "Contractura",
                "Fatiga",
                "Inflamación",
                "Calambre",
                "Molestia leve",
                "Lesión confirmada",
                "Otro"
        ));

        cmbContexto.setItems(FXCollections.observableArrayList(
                "Entrenamiento",
                "Partido",
                "Calentamiento",
                "Actividad externa",
                "Sin especificar"
        ));

        cmbEstado.setItems(FXCollections.observableArrayList(
                "Activa",
                "En seguimiento",
                "En reposo",
                "Reincorporación progresiva",
                "Recuperada"
        ));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdLesion()).asObject());

        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFecha() != null ? data.getValue().getFecha().toString() : ""
                ));

        colDeportista.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdDeportista()).asObject());

        colZona.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getZonaAfectada()));

        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipoMolestia()));

        colContexto.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getContexto()));

        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado()));

        colRegreso.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFechaRegresoEstimada() != null
                                ? data.getValue().getFechaRegresoEstimada().toString()
                                : ""
                ));
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

    private void cargarLesiones() {
        tablaLesiones.setItems(
                FXCollections.observableArrayList(lesionService.listarTodas())
        );
    }

    private Lesion leerFormulario() {
        Deportista deportista = cmbDeportista.getValue();

        Lesion l = new Lesion();
        l.setIdDeportista(deportista != null ? deportista.getIdDeportista() : 0);
        l.setFecha(dpFecha.getValue());
        l.setZonaAfectada(cmbZonaAfectada.getValue());
        l.setTipoMolestia(cmbTipoMolestia.getValue());
        l.setContexto(cmbContexto.getValue());
        l.setAccionTomada(txtAccionTomada.getText());
        l.setFechaRegresoEstimada(dpFechaRegreso.getValue());
        l.setEstado(cmbEstado.getValue());
        l.setObservaciones(txtObservaciones.getText());
        return l;
    }

    private void cargarLesionSeleccionada(Lesion l) {
        if (l == null) {
            return;
        }

        lesionSeleccionada = l;

        dpFecha.setValue(l.getFecha());
        cmbZonaAfectada.setValue(l.getZonaAfectada());
        cmbTipoMolestia.setValue(l.getTipoMolestia());
        cmbContexto.setValue(l.getContexto());
        txtAccionTomada.setText(l.getAccionTomada());
        dpFechaRegreso.setValue(l.getFechaRegresoEstimada());
        cmbEstado.setValue(l.getEstado());
        txtObservaciones.setText(l.getObservaciones());

        for (Deportista deportista : cmbDeportista.getItems()) {
            if (deportista.getIdDeportista() == l.getIdDeportista()) {
                cmbDeportista.setValue(deportista);
                break;
            }
        }
    }
}