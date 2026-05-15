package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.DashboardResumen;
import com.example.sportmanagerpro.services.ReporteService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label lblTotalDeportistas;
    @FXML private Label lblDeportistasActivas;
    @FXML private Label lblCategorias;
    @FXML private Label lblEntrenamientos;
    @FXML private Label lblPartidos;
    @FXML private Label lblAsistenciaGeneral;
    @FXML private Label lblVictorias;
    @FXML private Label lblEmpates;
    @FXML private Label lblDerrotas;
    @FXML private Label lblLesionesActivas;

    private final ReporteService reporteService = new ReporteService();

    @FXML
    public void initialize() {
        cargarDashboard();
    }

    @FXML
    private void cargarDashboard() {
        try {
            DashboardResumen resumen = reporteService.obtenerDashboardResumen();

            lblTotalDeportistas.setText(String.valueOf(resumen.getTotalDeportistas()));
            lblDeportistasActivas.setText(String.valueOf(resumen.getDeportistasActivas()));
            lblCategorias.setText(String.valueOf(resumen.getTotalCategorias()));
            lblEntrenamientos.setText(String.valueOf(resumen.getTotalEntrenamientos()));
            lblPartidos.setText(String.valueOf(resumen.getTotalPartidos()));

            lblAsistenciaGeneral.setText(String.format("%.1f %%", resumen.getPorcentajeAsistencia()));

            lblVictorias.setText(String.valueOf(resumen.getVictorias()));
            lblEmpates.setText(String.valueOf(resumen.getEmpates()));
            lblDerrotas.setText(String.valueOf(resumen.getDerrotas()));
            lblLesionesActivas.setText(String.valueOf(resumen.getLesionesActivas()));

        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }
}