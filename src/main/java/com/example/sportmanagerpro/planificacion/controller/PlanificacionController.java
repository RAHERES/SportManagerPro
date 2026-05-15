package com.example.sportmanagerpro.planificacion.controller;

import com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoModeloPlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoPeriodizacion;
import com.example.sportmanagerpro.planificacion.model.BloqueMacrociclo;
import com.example.sportmanagerpro.planificacion.service.BloquesPorPeriodizacionService;
import com.example.sportmanagerpro.planificacion.util.DistribucionTiempoUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PlanificacionController {


    @FXML
    private ComboBox<TipoModeloPlanificacion> cmbModeloPlanificacion;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaCompetencia;

    @FXML
    private TextField txtMinutosMicrociclo;

    @FXML
    private TextField txtCantidadMicrociclos;

    @FXML
    private TextArea txtResultado;

    @FXML
    private void initialize() {
        cmbModeloPlanificacion.getItems().setAll(TipoModeloPlanificacion.values());


    }



    @FXML
    private void generarPlanificacion() {
        int minutos = Integer.parseInt(txtMinutosMicrociclo.getText());
        int microciclos = Integer.parseInt(txtCantidadMicrociclos.getText());

        int tiempoTotal = DistribucionTiempoUtil.calcularTiempoTotal(minutos, microciclos);
        double tiempoFisico = DistribucionTiempoUtil.calcularTiempoFisico(tiempoTotal, 0.70);

        double sumaPorcentajes = 255;
        double ci = DistribucionTiempoUtil.calcularCI(tiempoFisico, sumaPorcentajes);

        double micro1 = DistribucionTiempoUtil.distribuirTiempo(60, ci);
        double micro2 = DistribucionTiempoUtil.distribuirTiempo(70, ci);
        double micro3 = DistribucionTiempoUtil.distribuirTiempo(75, ci);
        double micro4 = DistribucionTiempoUtil.distribuirTiempo(50, ci);

        txtResultado.setText(
                "Modelo seleccionado: " + cmbModeloPlanificacion.getValue() + "\n\n" +
                "Tiempo total del bloque: " + tiempoTotal + " minutos\n" +
                "Tiempo de preparación física: " + tiempoFisico + " minutos\n" +
                "Coeficiente indicativo: " + String.format("%.2f", ci) + "\n\n" +
                "Distribución por microciclo:\n" +
                "Microciclo 1: " + String.format("%.0f", micro1) + " min\n" +
                "Microciclo 2: " + String.format("%.0f", micro2) + " min\n" +
                "Microciclo 3: " + String.format("%.0f", micro3) + " min\n" +
                "Microciclo 4: " + String.format("%.0f", micro4) + " min\n"
        );
    }
}