package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Categoria;
import com.example.sportmanagerpro.models.ReporteCategoriaResumen;
import com.example.sportmanagerpro.services.CategoriaService;
import com.example.sportmanagerpro.services.ReporteService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class ReporteCategoriaController {

    @FXML private ComboBox<Categoria> cmbCategoria;

    @FXML private Label lblTotalDeportistas;
    @FXML private Label lblDeportistasActivas;
    @FXML private Label lblEntrenamientos;
    @FXML private Label lblAsistencia;
    @FXML private Label lblPartidos;
    @FXML private Label lblEvaluaciones;
    @FXML private Label lblVictorias;
    @FXML private Label lblEmpates;
    @FXML private Label lblDerrotas;
    @FXML private Label lblLesionesActivas;

    private final CategoriaService categoriaService = new CategoriaService();
    private final ReporteService reporteService = new ReporteService();

    @FXML
    public void initialize() {
        cargarCategorias();
        limpiarDatos();
    }

    @FXML
    private void cargarReporte() {
        Categoria categoria = cmbCategoria.getValue();

        if (categoria == null) {
            limpiarDatos();
            return;
        }

        try {
            ReporteCategoriaResumen resumen =
                    reporteService.obtenerResumenCategoria(categoria.getIdCategoria());

            lblTotalDeportistas.setText(String.valueOf(resumen.getTotalDeportistas()));
            lblDeportistasActivas.setText(String.valueOf(resumen.getDeportistasActivas()));
            lblEntrenamientos.setText(String.valueOf(resumen.getEntrenamientos()));
            lblAsistencia.setText(String.format("%.1f %%", resumen.getPorcentajeAsistencia()));
            lblPartidos.setText(String.valueOf(resumen.getPartidos()));
            lblEvaluaciones.setText(String.valueOf(resumen.getEvaluacionesFisicas()));
            lblVictorias.setText(String.valueOf(resumen.getVictorias()));
            lblEmpates.setText(String.valueOf(resumen.getEmpates()));
            lblDerrotas.setText(String.valueOf(resumen.getDerrotas()));
            lblLesionesActivas.setText(String.valueOf(resumen.getLesionesActivas()));

        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    private void cargarCategorias() {
        cmbCategoria.setItems(
                FXCollections.observableArrayList(categoriaService.listarTodas())
        );

        cmbCategoria.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Categoria categoria, boolean empty) {
                super.updateItem(categoria, empty);
                setText(empty || categoria == null ? null : categoria.getNombre());
            }
        });

        cmbCategoria.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Categoria categoria, boolean empty) {
                super.updateItem(categoria, empty);
                setText(empty || categoria == null ? null : categoria.getNombre());
            }
        });
    }

    private void limpiarDatos() {
        lblTotalDeportistas.setText("0");
        lblDeportistasActivas.setText("0");
        lblEntrenamientos.setText("0");
        lblAsistencia.setText("0 %");
        lblPartidos.setText("0");
        lblEvaluaciones.setText("0");
        lblVictorias.setText("0");
        lblEmpates.setText("0");
        lblDerrotas.setText("0");
        lblLesionesActivas.setText("0");
    }
}