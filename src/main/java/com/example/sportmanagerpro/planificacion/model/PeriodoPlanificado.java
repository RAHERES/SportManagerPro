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

    public void setTipoPeriodo(TipoPeriodoPlanificacion tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public int getSemanaInicio() {
        return semanaInicio;
    }

    public void setSemanaInicio(int semanaInicio) {
        this.semanaInicio = semanaInicio;
        recalcularDuracion();
    }

    public int getSemanaFin() {
        return semanaFin;
    }

    public void setSemanaFin(int semanaFin) {
        this.semanaFin = semanaFin;
        recalcularDuracion();
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public void setDuracionSemanas(int duracionSemanas) {
        this.duracionSemanas = duracionSemanas;
        this.semanaFin = this.semanaInicio + duracionSemanas - 1;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    private void recalcularDuracion() {
        this.duracionSemanas = this.semanaFin - this.semanaInicio + 1;
    }
}