package com.example.sportmanagerpro.planificacion.controller;


import com.example.sportmanagerpro.planificacion.enums.EstadoSesion;
import com.example.sportmanagerpro.planificacion.enums.MotivoIncidencia;
import com.example.sportmanagerpro.planificacion.model.ConfiguracionDiaEntrenamiento;
import com.example.sportmanagerpro.planificacion.model.Microciclo;
import com.example.sportmanagerpro.planificacion.model.ResumenMicrociclo;
import com.example.sportmanagerpro.planificacion.model.SesionPlanificada;
import com.example.sportmanagerpro.planificacion.service.AnalisisPlanificacionService;
import com.example.sportmanagerpro.planificacion.service.CalendarioEntrenamientoService;
import com.example.sportmanagerpro.planificacion.service.RegistroSesionService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador inicial del módulo de planificación.
 * Genera microciclos, sesiones planificadas y permite registrar una sesión de prueba.
 */
public class PlanificacionController2 {

    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;

    @FXML private TableView<Microciclo> tablaMicrociclos;
    @FXML private TableColumn<Microciclo, String> colNumero;
    @FXML private TableColumn<Microciclo, String> colFechas;
    @FXML private TableColumn<Microciclo, String> colTipo;
    @FXML private TableColumn<Microciclo, String> colSesiones;
    @FXML private TableColumn<Microciclo, String> colMinPlan;
    @FXML private TableColumn<Microciclo, String> colMinReal;
    @FXML private TableColumn<Microciclo, String> colCumplimiento;

    private final CalendarioEntrenamientoService calendarioService = new CalendarioEntrenamientoService();
    private final RegistroSesionService registroService = new RegistroSesionService();
    private final AnalisisPlanificacionService analisisService = new AnalisisPlanificacionService();

    private final List<Microciclo> microciclos = new ArrayList<>();

    @FXML
    private void initialize() {
        dpInicio.setValue(LocalDate.now());
        dpFin.setValue(LocalDate.now().plusMonths(1));

        configurarTabla();
    }

    /**
     * Configura las columnas de la tabla de microciclos.
     */
    private void configurarTabla() {
        colNumero.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getNumero())));

        colFechas.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFechaInicio() + " al " + data.getValue().getFechaFin()
                ));

        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipo().name()));

        colSesiones.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getSesiones().size())));

        colMinPlan.setCellValueFactory(data -> {
            ResumenMicrociclo resumen = analisisService.analizarMicrociclo(data.getValue());
            return new SimpleStringProperty(String.valueOf(resumen.getMinutosPlanificados()));
        });

        colMinReal.setCellValueFactory(data -> {
            ResumenMicrociclo resumen = analisisService.analizarMicrociclo(data.getValue());
            return new SimpleStringProperty(String.valueOf(resumen.getMinutosRealizados()));
        });

        colCumplimiento.setCellValueFactory(data -> {
            ResumenMicrociclo resumen = analisisService.analizarMicrociclo(data.getValue());
            return new SimpleStringProperty(String.format("%.1f %%", resumen.getCumplimiento()));
        });
    }

    /**
     * Genera microciclos y sesiones usando una configuración semanal variable.
     */
    @FXML
    private void generarPlan() {
        microciclos.clear();

        LocalDate inicio = dpInicio.getValue();
        LocalDate fin = dpFin.getValue();

        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            return;
        }

        microciclos.addAll(calendarioService.generarMicrociclos(inicio, fin));

        List<ConfiguracionDiaEntrenamiento> configuracion = crearConfiguracionSemanalEjemplo();

        for (Microciclo microciclo : microciclos) {
            calendarioService.generarSesiones(microciclo, configuracion);
        }

        tablaMicrociclos.getItems().setAll(microciclos);
    }

    /**
     * Registra como parcial la primera sesión del primer microciclo.
     */
    @FXML
    private void registrarSesionEjemplo() {
        if (microciclos.isEmpty()) {
            return;
        }

        Microciclo primerMicrociclo = microciclos.get(0);

        if (primerMicrociclo.getSesiones().isEmpty()) {
            return;
        }

        SesionPlanificada sesion = primerMicrociclo.getSesiones().get(0);

        registroService.registrarSesion(
                sesion,
                EstadoSesion.REALIZADA_PARCIAL,
                LocalTime.of(16, 40),
                LocalTime.of(17, 50),
                6,
                MotivoIncidencia.EVENTO_ESCOLAR,
                "La sesión inició tarde y se redujo la parte final."
        );

        actualizarAnalisis();
    }

    /**
     * Actualiza la tabla para recalcular los indicadores.
     */
    @FXML
    private void actualizarAnalisis() {
        tablaMicrociclos.refresh();
    }

    /**
     * Configuración semanal variable.
     * Aquí después lo cambiaremos para que se capture desde la interfaz.
     */
    private List<ConfiguracionDiaEntrenamiento> crearConfiguracionSemanalEjemplo() {
        return List.of(
                new ConfiguracionDiaEntrenamiento(DayOfWeek.MONDAY, true, LocalTime.of(16, 30), 120),
                new ConfiguracionDiaEntrenamiento(DayOfWeek.TUESDAY, true, LocalTime.of(16, 30), 90),
                new ConfiguracionDiaEntrenamiento(DayOfWeek.THURSDAY, true, LocalTime.of(16, 30), 60),
                new ConfiguracionDiaEntrenamiento(DayOfWeek.SATURDAY, true, LocalTime.of(14, 0), 180)
        );
    }
}