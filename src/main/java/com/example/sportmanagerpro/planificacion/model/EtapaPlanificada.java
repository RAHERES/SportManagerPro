package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoEtapaPlanificacion;

import java.time.LocalDate;

/**
 * Representa una etapa dentro del plan gráfico.
 */
public class EtapaPlanificada {

    private TipoEtapaPlanificacion tipoEtapa;
    private int semanaInicio;
    private int semanaFin;
    private int duracionSemanas;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeDentroPeriodo;

    public EtapaPlanificada(TipoEtapaPlanificacion tipoEtapa,
                            int semanaInicio,
                            int semanaFin,
                            LocalDate fechaInicio,
                            LocalDate fechaFin,
                            double porcentajeDentroPeriodo) {
        this.tipoEtapa = tipoEtapa;
        this.semanaInicio = semanaInicio;
        this.semanaFin = semanaFin;
        this.duracionSemanas = semanaFin - semanaInicio + 1;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeDentroPeriodo = porcentajeDentroPeriodo;
    }

    public TipoEtapaPlanificacion getTipoEtapa() {
        return tipoEtapa;
    }

    public void setTipoEtapa(TipoEtapaPlanificacion tipoEtapa) {
        this.tipoEtapa = tipoEtapa;
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

    public double getPorcentajeDentroPeriodo() {
        return porcentajeDentroPeriodo;
    }

    public void setPorcentajeDentroPeriodo(double porcentajeDentroPeriodo) {
        this.porcentajeDentroPeriodo = porcentajeDentroPeriodo;
    }

    private void recalcularDuracion() {
        this.duracionSemanas = this.semanaFin - this.semanaInicio + 1;
    }
}