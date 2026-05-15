package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Categoria;
import com.example.sportmanagerpro.models.Entrenamiento;
import com.example.sportmanagerpro.services.CategoriaService;
import com.example.sportmanagerpro.services.EntrenamientoService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalTime;

public class EntrenamientoController {

    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private ComboBox<String> cmbIntensidad;
    @FXML private TextField txtDuracion;
    @FXML private TextArea txtObjetivo;
    @FXML private TextArea txtContenido;
    @FXML private TextArea txtMaterial;
    @FXML private TextArea txtObservaciones;

    @FXML private TableView<Entrenamiento> tablaEntrenamientos;
    @FXML private TableColumn<Entrenamiento, Integer> colId;
    @FXML private TableColumn<Entrenamiento, String> colFecha;
    @FXML private TableColumn<Entrenamiento, Integer> colCategoria;
    @FXML private TableColumn<Entrenamiento, String> colTipo;
    @FXML private TableColumn<Entrenamiento, String> colIntensidad;
    @FXML private TableColumn<Entrenamiento, Integer> colDuracion;
    @FXML private TableColumn<Entrenamiento, String> colObjetivo;

    private final EntrenamientoService entrenamientoService = new EntrenamientoService();
    private final CategoriaService categoriaService = new CategoriaService();

    private Entrenamiento entrenamientoSeleccionado;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        cargarCategorias();
        cargarEntrenamientos();

        tablaEntrenamientos.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> cargarEntrenamientoSeleccionado(seleccionado)
        );
    }

    @FXML
    private void guardarEntrenamiento() {
        try {
            Entrenamiento entrenamiento = leerFormulario();
            entrenamientoService.guardar(entrenamiento);
            AlertUtils.mostrarInformacion("Entrenamiento guardado correctamente.");
            limpiarFormulario();
            cargarEntrenamientos();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarEntrenamiento() {
        if (entrenamientoSeleccionado == null) {
            AlertUtils.mostrarError("Debe seleccionar un entrenamiento.");
            return;
        }

        try {
            Entrenamiento entrenamiento = leerFormulario();
            entrenamiento.setIdEntrenamiento(entrenamientoSeleccionado.getIdEntrenamiento());
            entrenamientoService.actualizar(entrenamiento);
            AlertUtils.mostrarInformacion("Entrenamiento actualizado correctamente.");
            limpiarFormulario();
            cargarEntrenamientos();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        entrenamientoSeleccionado = null;
        dpFecha.setValue(null);
        txtHoraInicio.clear();
        txtHoraFin.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        cmbTipo.getSelectionModel().clearSelection();
        cmbIntensidad.getSelectionModel().clearSelection();
        txtDuracion.clear();
        txtObjetivo.clear();
        txtContenido.clear();
        txtMaterial.clear();
        txtObservaciones.clear();
        tablaEntrenamientos.getSelectionModel().clearSelection();
    }

    private void configurarCombos() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Técnico",
                "Táctico",
                "Físico",
                "Fuerza",
                "Velocidad",
                "Resistencia",
                "Agilidad",
                "Coordinación",
                "Recuperación",
                "Partido interescuadras"
        ));

        cmbIntensidad.setItems(FXCollections.observableArrayList(
                "Baja",
                "Media",
                "Alta",
                "Muy alta"
        ));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdEntrenamiento()).asObject());

        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFecha().toString()));

        colCategoria.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdCategoria()).asObject());

        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipoEntrenamiento()));

        colIntensidad.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getIntensidad()));

        colDuracion.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getDuracionMinutos()).asObject());

        colObjetivo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getObjetivo()));
    }

    private void cargarCategorias() {
        cmbCategoria.setItems(FXCollections.observableArrayList(categoriaService.listarTodas()));
    }

    private void cargarEntrenamientos() {
        tablaEntrenamientos.setItems(
                FXCollections.observableArrayList(entrenamientoService.listarTodos())
        );
    }

    private Entrenamiento leerFormulario() {
        Categoria categoria = cmbCategoria.getValue();

        Entrenamiento e = new Entrenamiento();
        e.setFecha(dpFecha.getValue());
        e.setHoraInicio(parseHora(txtHoraInicio.getText()));
        e.setHoraFin(parseHora(txtHoraFin.getText()));
        e.setIdCategoria(categoria != null ? categoria.getIdCategoria() : 0);
        e.setTipoEntrenamiento(cmbTipo.getValue());
        e.setIntensidad(cmbIntensidad.getValue());
        e.setDuracionMinutos(parseEntero(txtDuracion.getText()));
        e.setObjetivo(txtObjetivo.getText());
        e.setContenidoTrabajado(txtContenido.getText());
        e.setMaterialUtilizado(txtMaterial.getText());
        e.setObservaciones(txtObservaciones.getText());

        return e;
    }

    private void cargarEntrenamientoSeleccionado(Entrenamiento e) {
        if (e == null) {
            return;
        }

        entrenamientoSeleccionado = e;

        dpFecha.setValue(e.getFecha());
        txtHoraInicio.setText(e.getHoraInicio() != null ? e.getHoraInicio().toString() : "");
        txtHoraFin.setText(e.getHoraFin() != null ? e.getHoraFin().toString() : "");
        cmbTipo.setValue(e.getTipoEntrenamiento());
        cmbIntensidad.setValue(e.getIntensidad());
        txtDuracion.setText(String.valueOf(e.getDuracionMinutos()));
        txtObjetivo.setText(e.getObjetivo());
        txtContenido.setText(e.getContenidoTrabajado());
        txtMaterial.setText(e.getMaterialUtilizado());
        txtObservaciones.setText(e.getObservaciones());

        for (Categoria categoria : cmbCategoria.getItems()) {
            if (categoria.getIdCategoria() == e.getIdCategoria()) {
                cmbCategoria.setValue(categoria);
                break;
            }
        }
    }

    private LocalTime parseHora(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalTime.parse(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("La hora debe tener formato HH:mm. Ejemplo: 16:30");
        }
    }

    private int parseEntero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("La duración debe ser un número entero.");
        }
    }
}