package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoPeriodoPlanificacion;

import java.time.LocalDate;

/**
 * Representa un periodo calculado dentro del plan gráfico.
 */
public class PeriodoPlanificado {

    private double porcentaje;

    private TipoPeriodoPlanificacion tipoPeriodo;
    private int semanaInicio;
    private int semanaFin;
    private int duracionSemanas;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public PeriodoPlanificado(TipoPeriodoPlanificacion tipoPeriodo,
                              int semanaInicio,
                              int semanaFin,
                              LocalDate fechaInicio,
                              LocalDate fechaFin,
                              double porcentaje) {
        this.tipoPeriodo = tipoPeriodo;
        this.semanaInicio = semanaInicio;
        this.semanaFin = semanaFin;
        this.duracionSemanas = semanaFin - semanaInicio + 1;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentaje = porcentaje;
    }

    public TipoPeriodoPlanificacion getTipoPeriodo() {
        return tipoPeriodo;
    }

    public int getSemanaInicio() {
        return semanaInicio;
    }

    public int getSemanaFin() {
        return semanaFin;
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public double getPorcentaje() {
        return porcentaje;
    }
}