package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.*;
import com.example.sportmanagerpro.services.DeportistaService;
import com.example.sportmanagerpro.services.EstadisticaPartidoService;
import com.example.sportmanagerpro.services.PartidoService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class EstadisticaPartidoController {

    @FXML private ComboBox<Partido> cmbPartido;

    @FXML private TableView<EstadisticaPartidoRow> tablaEstadisticas;
    @FXML private TableColumn<EstadisticaPartidoRow, String> colNombre;
    @FXML private TableColumn<EstadisticaPartidoRow, Boolean> colTitular;
    @FXML private TableColumn<EstadisticaPartidoRow, Integer> colMinutos;
    @FXML private TableColumn<EstadisticaPartidoRow, Integer> colGoles;
    @FXML private TableColumn<EstadisticaPartidoRow, Integer> colAsistencias;
    @FXML private TableColumn<EstadisticaPartidoRow, Integer> colAmarillas;
    @FXML private TableColumn<EstadisticaPartidoRow, Integer> colRojas;
    @FXML private TableColumn<EstadisticaPartidoRow, String> colObservaciones;

    private final PartidoService partidoService = new PartidoService();
    private final DeportistaService deportistaService = new DeportistaService();
    private final EstadisticaPartidoService estadisticaService = new EstadisticaPartidoService();

    @FXML
    public void initialize() {
        tablaEstadisticas.setEditable(true);
        configurarTabla();
        cargarPartidos();
    }

    @FXML
    private void cargarDeportistasDelPartido() {
        Partido partido = cmbPartido.getValue();

        if (partido == null) {
            return;
        }

        tablaEstadisticas.getItems().clear();

        var deportistas = deportistaService.listarPorCategoria(partido.getIdCategoria());

        for (Deportista deportista : deportistas) {
            tablaEstadisticas.getItems().add(new EstadisticaPartidoRow(deportista));
        }
    }

    @FXML
    private void guardarEstadisticas() {
        Partido partido = cmbPartido.getValue();

        if (partido == null) {
            AlertUtils.mostrarError("Debe seleccionar un partido.");
            return;
        }

        try {
            for (EstadisticaPartidoRow row : tablaEstadisticas.getItems()) {
                EstadisticaPartido estadistica = new EstadisticaPartido();

                estadistica.setIdPartido(partido.getIdPartido());
                estadistica.setIdDeportista(row.getDeportista().getIdDeportista());
                estadistica.setTitular(row.isTitular());
                estadistica.setMinutosJugados(row.getMinutosJugados());
                estadistica.setGoles(row.getGoles());
                estadistica.setAsistencias(row.getAsistencias());
                estadistica.setTarjetasAmarillas(row.getTarjetasAmarillas());
                estadistica.setTarjetasRojas(row.getTarjetasRojas());
                estadistica.setObservaciones(row.getObservaciones());

                estadisticaService.guardar(estadistica);
            }

            AlertUtils.mostrarInformacion("Estadísticas guardadas correctamente.");

        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDeportista().getNombreCompleto()));

        colTitular.setCellValueFactory(data ->
                data.getValue().titularProperty());

        colTitular.setCellFactory(CheckBoxTableCell.forTableColumn(colTitular));

        colMinutos.setCellValueFactory(data ->
                data.getValue().minutosJugadosProperty().asObject());

        colGoles.setCellValueFactory(data ->
                data.getValue().golesProperty().asObject());

        colAsistencias.setCellValueFactory(data ->
                data.getValue().asistenciasProperty().asObject());

        colAmarillas.setCellValueFactory(data ->
                data.getValue().tarjetasAmarillasProperty().asObject());

        colRojas.setCellValueFactory(data ->
                data.getValue().tarjetasRojasProperty().asObject());

        colObservaciones.setCellValueFactory(data ->
                data.getValue().observacionesProperty());

        configurarEdicionEntera(colMinutos);
        configurarEdicionEntera(colGoles);
        configurarEdicionEntera(colAsistencias);
        configurarEdicionEntera(colAmarillas);
        configurarEdicionEntera(colRojas);

        colObservaciones.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void configurarEdicionEntera(TableColumn<EstadisticaPartidoRow, Integer> columna) {
        columna.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    }

    private void cargarPartidos() {
        cmbPartido.setItems(FXCollections.observableArrayList(partidoService.listarTodos()));

        cmbPartido.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Partido partido, boolean empty) {
                super.updateItem(partido, empty);
                setText(empty || partido == null ? null : formatearPartido(partido));
            }
        });

        cmbPartido.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Partido partido, boolean empty) {
                super.updateItem(partido, empty);
                setText(empty || partido == null ? null : formatearPartido(partido));
            }
        });
    }

    private String formatearPartido(Partido partido) {
        return partido.getFecha() + " | " + partido.getRival() + " | "
                + partido.getMarcadorFavor() + " - " + partido.getMarcadorContra();
    }
}