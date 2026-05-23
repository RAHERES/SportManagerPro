package com.example.sportmanagerpro.planificacion.configuracion;

import java.time.LocalDate;

/**
 * Representa una competencia o evento competitivo dentro del calendario del ciclo.
 */
public class CompetenciaPlanificada {

    private String nombre;
    private TipoCompetencia tipoCompetencia;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String fase;
    private String sede;
    private String objetivo;
    private int prioridad;
    private boolean competenciaClave;
    private String observaciones;

    public CompetenciaPlanificada(String nombre,
                                  TipoCompetencia tipoCompetencia,
                                  LocalDate fechaInicio,
                                  LocalDate fechaFin,
                                  String fase,
                                  String sede,
                                  String objetivo,
                                  int prioridad,
                                  boolean competenciaClave,
                                  String observaciones) {
        this.nombre = nombre;
        this.tipoCompetencia = tipoCompetencia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fase = fase;
        this.sede = sede;
        this.objetivo = objetivo;
        this.prioridad = prioridad;
        this.competenciaClave = competenciaClave;
        this.observaciones = observaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCompetencia getTipoCompetencia() {
        return tipoCompetencia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getFase() {
        return fase;
    }

    public String getSede() {
        return sede;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public boolean isCompetenciaClave() {
        return competenciaClave;
    }

    public String getObservaciones() {
        return observaciones;
    }
}