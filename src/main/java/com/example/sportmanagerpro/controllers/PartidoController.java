package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Categoria;
import com.example.sportmanagerpro.models.Competencia;
import com.example.sportmanagerpro.models.Partido;
import com.example.sportmanagerpro.services.CategoriaService;
import com.example.sportmanagerpro.services.CompetenciaService;
import com.example.sportmanagerpro.services.PartidoService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PartidoController {

    @FXML private ComboBox<Competencia> cmbCompetencia;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtRival;
    @FXML private TextField txtSede;
    @FXML private ComboBox<String> cmbFase;
    @FXML private TextField txtMarcadorFavor;
    @FXML private TextField txtMarcadorContra;
    @FXML private TextArea txtObservaciones;

    @FXML private TableView<Partido> tablaPartidos;
    @FXML private TableColumn<Partido, Integer> colId;
    @FXML private TableColumn<Partido, String> colFecha;
    @FXML private TableColumn<Partido, String> colRival;
    @FXML private TableColumn<Partido, Integer> colCategoria;
    @FXML private TableColumn<Partido, String> colMarcador;
    @FXML private TableColumn<Partido, String> colResultado;
    @FXML private TableColumn<Partido, String> colFase;

    private final PartidoService partidoService = new PartidoService();
    private final CategoriaService categoriaService = new CategoriaService();
    private final CompetenciaService competenciaService = new CompetenciaService();

    private Partido partidoSeleccionado;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        cargarCompetencias();
        cargarCategorias();
        cargarPartidos();

        tablaPartidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> cargarPartidoSeleccionado(seleccionado)
        );
    }

    @FXML
    private void guardarPartido() {
        try {
            Partido partido = leerFormulario();
            partidoService.guardar(partido);
            AlertUtils.mostrarInformacion("Partido guardado correctamente.");
            limpiarFormulario();
            cargarPartidos();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarPartido() {
        if (partidoSeleccionado == null) {
            AlertUtils.mostrarError("Debe seleccionar un partido.");
            return;
        }

        try {
            Partido partido = leerFormulario();
            partido.setIdPartido(partidoSeleccionado.getIdPartido());
            partidoService.actualizar(partido);
            AlertUtils.mostrarInformacion("Partido actualizado correctamente.");
            limpiarFormulario();
            cargarPartidos();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        partidoSeleccionado = null;
        cmbCompetencia.getSelectionModel().clearSelection();
        cmbCategoria.getSelectionModel().clearSelection();
        dpFecha.setValue(null);
        txtRival.clear();
        txtSede.clear();
        cmbFase.getSelectionModel().clearSelection();
        txtMarcadorFavor.clear();
        txtMarcadorContra.clear();
        txtObservaciones.clear();
        tablaPartidos.getSelectionModel().clearSelection();
    }

    private void configurarCombos() {
        cmbFase.setItems(FXCollections.observableArrayList(
                "Fase de grupos",
                "Cuartos de final",
                "Semifinal",
                "Final",
                "Amistoso",
                "Liga regular",
                "Otro"
        ));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdPartido()).asObject());

        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFecha().toString()));

        colRival.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRival()));

        colCategoria.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdCategoria()).asObject());

        colMarcador.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getMarcadorFavor()
                                + " - "
                                + data.getValue().getMarcadorContra()
                ));

        colResultado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getResultado()));

        colFase.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFase()));
    }

    private void cargarCompetencias() {
        cmbCompetencia.setItems(
                FXCollections.observableArrayList(competenciaService.listarTodas())
        );
    }

    private void cargarCategorias() {
        cmbCategoria.setItems(
                FXCollections.observableArrayList(categoriaService.listarTodas())
        );
    }

    private void cargarPartidos() {
        tablaPartidos.setItems(
                FXCollections.observableArrayList(partidoService.listarTodos())
        );
    }

    private Partido leerFormulario() {
        Competencia competencia = cmbCompetencia.getValue();
        Categoria categoria = cmbCategoria.getValue();

        Partido p = new Partido();

        p.setIdCompetencia(competencia != null ? competencia.getIdCompetencia() : 0);
        p.setIdCategoria(categoria != null ? categoria.getIdCategoria() : 0);
        p.setFecha(dpFecha.getValue());
        p.setRival(txtRival.getText());
        p.setSede(txtSede.getText());
        p.setFase(cmbFase.getValue());
        p.setMarcadorFavor(parseEntero(txtMarcadorFavor.getText(), "marcador a favor"));
        p.setMarcadorContra(parseEntero(txtMarcadorContra.getText(), "marcador en contra"));
        p.setObservaciones(txtObservaciones.getText());

        return p;
    }

    private void cargarPartidoSeleccionado(Partido p) {
        if (p == null) {
            return;
        }

        partidoSeleccionado = p;

        dpFecha.setValue(p.getFecha());
        txtRival.setText(p.getRival());
        txtSede.setText(p.getSede());
        cmbFase.setValue(p.getFase());
        txtMarcadorFavor.setText(String.valueOf(p.getMarcadorFavor()));
        txtMarcadorContra.setText(String.valueOf(p.getMarcadorContra()));
        txtObservaciones.setText(p.getObservaciones());

        for (Competencia competencia : cmbCompetencia.getItems()) {
            if (competencia.getIdCompetencia() == p.getIdCompetencia()) {
                cmbCompetencia.setValue(competencia);
                break;
            }
        }

        for (Categoria categoria : cmbCategoria.getItems()) {
            if (categoria.getIdCategoria() == p.getIdCategoria()) {
                cmbCategoria.setValue(categoria);
                break;
            }
        }
    }

    private int parseEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El " + campo + " debe ser un número entero.");
        }
    }
}